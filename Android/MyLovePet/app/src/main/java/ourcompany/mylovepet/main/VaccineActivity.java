package ourcompany.mylovepet.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.CalendarView;


public class VaccineActivity extends AppCompatActivity {
    HashSet<Date> events;
    CalendarView cv;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    Button button1,button3;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);
        builder = new AlertDialog.Builder(this);
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
                Toast.makeText(VaccineActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                
            }
            @Override
            public void setEvents() {
                cv.updateCalendar(events);
            }
        });

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleMessage();
            }
        });

        button3 = (Button)findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlramMessage();
            }
        });
    }

    private void showScheduleMessage() {
        builder.setTitle("전체일정");
        builder.setMessage(events+"");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlramMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("알람을 On/Off");

        builder.setPositiveButton("On", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        builder.setNegativeButton("Off", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
