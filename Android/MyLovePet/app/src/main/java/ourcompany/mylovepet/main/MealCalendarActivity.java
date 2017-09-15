package ourcompany.mylovepet.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.CalendarView2;


public class MealCalendarActivity extends AppCompatActivity
{
    HashSet<Date> events;
    CalendarView2 cv;
    LinearLayout li;
    LinearLayout layout;
    EditText et;
    Date d;
    public static Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_cal_main);
        events = new HashSet<>(); // 원하는 날짜에 마커

        li = (LinearLayout)findViewById(R.id.layout_main);
        cv = ((CalendarView2)findViewById(R.id.calendar_view));
        layout = (LinearLayout)findViewById(R.id.layout_add);
        cv.updateCalendar(events);
        // assign event handler

        cv.setEventHandler(new CalendarView2.EventHandler() // 달력 날짜 리스너
        {
            @Override
            public void onDayLongPress(Date date)
            {
<<<<<<< HEAD
<<<<<<< HEAD
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

=======
                /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
>>>>>>> parent of 936c985... URL 클래스
=======
                /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
>>>>>>> parent of 936c985... URL 클래스
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
<<<<<<< HEAD
<<<<<<< HEAD
                layout.addView(et);
            }
            @Override
            public void setEvents() {
                cv.updateCalendar(events);
=======

>>>>>>> parent of 936c985... URL 클래스
=======

>>>>>>> parent of 936c985... URL 클래스
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

        findViewById(R.id.meal_update).setOnClickListener(new View.OnClickListener() { // 원하는 날짜에 마커 표시
            @Override

            public void onClick(View v) {
                Date a = new Date();
                int compare = a.compareTo(d);

                if(compare > 0){
                    events.add(d);
                    Toast.makeText(MealCalendarActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    cv.updateCalendar(events);
                }else if(compare < 0){
                    Toast.makeText(MealCalendarActivity.this, "미래를 보는가?", Toast.LENGTH_SHORT).show();
                }else{
                    events.add(d);
                    Toast.makeText(MealCalendarActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    cv.updateCalendar(events);

            public void preTask() {
                lockButton();
            }

            @Override
            public void postTask(byte[] bytes) {
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);
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
                        Toast.makeText(getApplicationContext(),"업로드 성공",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"업로드 실패",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e ) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }finally {
                    unLockButton();
                    calendarView.updateCalendar(localDateSetToDateSet(notes.keySet()));

                }

            }

        });
            //통신 메소드 end
        };

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
                .url("http://58.226.2.45/Servlet/animalMeal")
                .post(body)
                .build();

        new ServerTaskManager(request, noteUpdateTaskListener, getApplicationContext()).execute();
    }
}
