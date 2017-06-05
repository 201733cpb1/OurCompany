package ourcompany.mylovepet.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        events.add(new Date());
        events.add(getDate(2017,5,15));
        events.add(getDate(2017,6,5));

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
                layout.removeView(et);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                param.gravity = Gravity.CENTER;
                et = new EditText(getApplicationContext());
                et.setTextColor(Color.BLACK);
                et.setTextSize(13);
                et.setPadding(10,0,0,0);
                et.setLayoutParams(param);
                et.setText("");
                et.setBackgroundResource(R.drawable.rect);

                layout.addView(et);
                // show returned day
               /* DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(Meal_Calendar.this, df.format(date), Toast.LENGTH_SHORT).show();*/
            }
            @Override
            public void setEvents() {
                cv.updateCalendar(events);
            }
        });
    }
}
