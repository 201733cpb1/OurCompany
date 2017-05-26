package ourcompany.mylovepet.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.CalendarView;


public class MealCalendarActivity extends AppCompatActivity
{
    HashSet<Date> events;
    CalendarView cv;
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
        getDate(2017,5,15);
        events = new HashSet<>(); // 원하는 날짜에 마커
        events.add(new Date());
        events.add(getDate(2017,5,15));
        events.add(getDate(2017,6,5));

        cv = ((CalendarView)findViewById(R.id.calendar_view));
        cv.updateCalendar(events);

        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {

            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(MealCalendarActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void setEvents() {
                cv.updateCalendar(events);
            }
        });

    }
}
