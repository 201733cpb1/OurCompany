package com.example.kdm.mylovepet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout li;
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
        setContentView(R.layout.meal_cal_main);
        events = new HashSet<>(); // 원하는 날짜에 마커
        events.add(new Date());
        events.add(getDate(2017,5,15));
        events.add(getDate(2017,6,5));

        li = (LinearLayout)findViewById(R.id.layout_main);
        cv = ((CalendarView)findViewById(R.id.calendar_view));
        cv.updateCalendar(events);
        findViewById(R.id.z);
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {

            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(Meal_Calendar.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void setEvents() {
                cv.updateCalendar(events);
            }
        });

        findViewById(R.id.meal_add).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                final LinearLayout layout = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params2.gravity=Gravity.CENTER;
                layout.setLayoutParams(params);

                li.addView(layout);
                final ImageView iv = new ImageView(getApplicationContext());
                final EditText et = new EditText(getApplicationContext());

                iv.setImageResource(R.drawable.activemass);
                iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(iv);

                et.setTextColor(Color.BLACK);
                et.setCompoundDrawablePadding(10);
                et.setTextSize(13);
                et.setGravity(Gravity.CENTER);
                et.setPadding(10,0,0,0);
                et.setHeight(300);
                et.setWidth(880);
                et.setLayoutParams(params2);
                et.setBackgroundResource(R.drawable.rect);
                layout.addView(et);
            }
        });
    }
}
