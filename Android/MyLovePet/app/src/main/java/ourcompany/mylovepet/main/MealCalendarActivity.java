package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.CalendarView2;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;


public class MealCalendarActivity extends AppCompatActivity
{
    HashSet<Date> events;
    CalendarView2 calendarView;
    LinearLayout li;
    EditText et;
    Date d;

    LocalDate selectDate;

    ViewGroup editContainer;
    DateTimeFormatter dateTimeFormat;
    DateTimeFormatter dateTimeFormatDate;

    TaskListener getNoteTask;

    EditText editTextNote;

    int petNo;


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
        calendarView = ((CalendarView2)findViewById(R.id.calendar_view));
        calendarView.updateCalendar(events);
        // assign event handler

        Intent intent = getIntent();
        petNo = intent.getIntExtra("petNo",-1);

        editContainer = (ViewGroup) findViewById(R.id.editContainer);

        dateTimeFormatDate = DateTimeFormat.forPattern("YYYY-MM-dd");

        editTextNote = (EditText)findViewById(R.id.editTextNote);

        selectDate = LocalDate.now();

        taskInit();

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
                editContainer.setVisibility(View.VISIBLE);
                editTextNote.setText("");
                Toast.makeText(getApplicationContext(),selectDate.toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void setEvents() {
                calendarView.updateCalendar(events);
            }
        });

        updateNote(dateTimeFormatDate.print(selectDate),null);

        findViewById(R.id.meal_update).setOnClickListener(new View.OnClickListener() { // 원하는 날짜에 마커 표시
            @Override
            public void onClick(View v) {
                /*Date a = new Date();
                int compare = a.compareTo(d);

                if(compare > 0){
                    events.add(d);
                    Toast.makeText(MealCalendarActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    calendarView.updateCalendar(events);
                }else if(compare < 0){
                    Toast.makeText(MealCalendarActivity.this, "미래를 보는가?", Toast.LENGTH_SHORT).show();
                }else{
                    events.add(d);
                    Toast.makeText(MealCalendarActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    calendarView.updateCalendar(events);
                }*/

                //업데이트 코드 작성 @@
                registerNote();
            }
        });
    }


    private void taskInit(){
        getNoteTask = new TaskListener() {
            @Override
            public void preTask() {

            }

            @Override
            public void postTask(Response response) {

            }

            @Override
            public void cancelTask() {

            }

            @Override
            public void fairTask() {

            }
        };


    }


    private void registerNote(){
        String date = dateTimeFormat.print(selectDate);
        String text = editTextNote.getText().toString();
        updateNote(date,text);
    }

    private void updateNote(String date, String text){

        FormBody.Builder builder= new FormBody.Builder()
                .add("animalNo", petNo+"")
                .add("month",date);
        if(text != null){
            builder.add("text",text);
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/animalMeal")
                .post(body)
                .build();

        new RequestTask(request,getNoteTask,getApplicationContext()).execute();
    }




}
