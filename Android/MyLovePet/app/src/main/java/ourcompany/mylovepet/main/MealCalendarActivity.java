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
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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

                layout.addView(et);
            }
            @Override
            public void setEvents() {
                cv.updateCalendar(events);
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
                }

            }
        });
    }
}
