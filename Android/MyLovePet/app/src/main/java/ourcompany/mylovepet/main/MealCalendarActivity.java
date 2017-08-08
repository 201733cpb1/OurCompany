package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.CalendarView2;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;


public class MealCalendarActivity extends AppCompatActivity implements TaskListener {
    CalendarView2 calendarView;
    LinearLayout li;

    LocalDate selectDate;

    ViewGroup editContainer;
    DateTimeFormatter dateTimeFormatDate;

    Button buttonUpdate;
    EditText editTextNote;

    int petNo;

    HashMap<LocalDate,String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        petNo = intent.getIntExtra("petNo",-1);

        setContentView(R.layout.activity_meal_cal_main);

        notes = new HashMap<>();
        li = (LinearLayout)findViewById(R.id.layout_main);
        calendarView = ((CalendarView2)findViewById(R.id.calendar_view));
        calendarView.updateCalendar(localDateSetToDateSet(notes.keySet()));
        // assign event handler

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("펫 먹이노트");
        actionBar.setDisplayHomeAsUpEnabled(true);

        editContainer = (ViewGroup) findViewById(R.id.editContainer);
        editTextNote = (EditText)findViewById(R.id.editTextNote);

        dateTimeFormatDate = DateTimeFormat.forPattern("YYYY-MM-dd");
        selectDate = LocalDate.now();

        listenerInit();

        noteUpdate(dateTimeFormatDate.print(selectDate),null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void listenerInit(){

        buttonUpdate = (Button)findViewById(R.id.meal_update);

        buttonUpdate.setOnClickListener(new View.OnClickListener() { // 원하는 날짜에 마커 표시
            @Override
            public void onClick(View v) {
                //노트 업로드
                String date = dateTimeFormatDate.print(selectDate);
                String text = editTextNote.getText().toString();
                noteUpdate(date,text);
            }
        });

        calendarView.setEventHandler(new CalendarView2.EventHandler() // 달력 날짜 리스너
        {
            @Override
            public void onDayLongPress(Date date)
            {
                /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                layout.removeView(et);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                param.gravity = Gravity.CENTER;
                et = new EditText(getApplicationContext());
                et.setTextColor(Color.BLACK);
                et.setTextSize(13);
                et.setPadding(10,0,0,0);
                et.setLayoutParams(param);

                et.setText(""); // 입력한 먹이 출력
                et.setBackgroundResource(R.drawable.rect);

                d = date; // 선택한 날

                layout.addView(et);*/
                selectDate = LocalDate.fromDateFields(date);
                String note = notes.get(selectDate);

                if(note == null){
                    editTextNote.setText("");
                }else {
                    editTextNote.setText(note);
                }

                Toast.makeText(getApplicationContext(),selectDate.toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void setEvents() {
                HashSet<Date> dateSets = localDateSetToDateSet(notes.keySet());
                calendarView.updateCalendar(dateSets);
            }
        });


    }

    //툴바에 있는 뒤로가기 버튼이 눌렀을때 해야할 동작을 정의
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private HashSet<Date> localDateSetToDateSet(Set<LocalDate> localDateSets){
        HashSet<Date> dateSets = new HashSet<>();

        for(LocalDate date: localDateSets){
            dateSets.add(date.toDate());
        }
        return dateSets;
    }

    private void lockButton(){
        buttonUpdate.setEnabled(false);
    }

    private void unLockButton(){
        buttonUpdate.setEnabled(true);
    }


    private void noteUpdate(String date, String text){
        FormBody.Builder builder= new FormBody.Builder()
                .add("animalNo", petNo+"")
                .add("date",date);
        if(text != null){
            builder.add("text",text);
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/animalMeal")
                .post(body)
                .build();

        new RequestTask(request,this,getApplicationContext()).execute();
    }

    //통신 메소드
    @Override
    public void preTask() {
        lockButton();
    }

    @Override
    public void postTask(Response response) {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            jsonObject = jsonObject.getJSONObject("report");
            boolean result = jsonObject.getBoolean("result");
            if(result){
                notes = new HashMap<>();
                JSONArray jsonArray = jsonObject.getJSONArray("value");
                for(int i = 0 ; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    LocalDate localDate = LocalDate.parse(jsonObject.getString("Date"));
                    String note = jsonObject.getString("Text");
                    notes.put(localDate, note);
                }
                Toast.makeText(this,"업로드 성공",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"업로드 실패",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e ) {
            e.printStackTrace();
            Toast.makeText(this, "서버 통신 오류", Toast.LENGTH_SHORT).show();
        }finally {
            unLockButton();
            calendarView.updateCalendar(localDateSetToDateSet(notes.keySet()));
        }
    }

    @Override
    public void cancelTask() {
        unLockButton();
    }

    @Override
    public void fairTask() {
        unLockButton();
        Toast.makeText(this,"업로드 실패 다시 시도해주세요",Toast.LENGTH_SHORT).show();
    }
    //통신 메소드 end

}
