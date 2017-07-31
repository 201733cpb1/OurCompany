package ourcompany.mylovepet.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.CalendarView;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;


public class VaccineActivity extends AppCompatActivity implements TaskListener{
    HashSet<Date> events;
    CalendarView cv;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Button button1,button3;
    AlertDialog.Builder builder2;

    String dDay = "";
    TextView textViewdday;

    //String[] totalSchedule = new String[6];
    ArrayList<String> totalSchedule = new ArrayList<>();
    ArrayList<Date> dateArrayList = new ArrayList<>();

    public static Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    public void dayCalculation(int count,Date date){ // d-day 계산
        String[] token = df.format(date).split("-");
        int year = Integer.parseInt(token[0]);
        int month = Integer.parseInt(token[1]);
        int day = Integer.parseInt(token[2]);
        dateArrayList.removeAll(dateArrayList);
        dateArrayList.add(getDate(year,month,day));
        int j = 14;
        for(int i = count+1;i<6;i++){
            dateArrayList.add(getDate(year,month,day+j));
            j +=14;
        }

        Date nowDate = new Date();

        while(true){
            if(dateArrayList.get(0).getDate() == nowDate.getDate()){
                dDay = "day";
                textViewdday.setText("접종날 D-"+dDay);
                break;
            }
            int compare = nowDate.compareTo(dateArrayList.get(0));

            if(compare > 0){
                if(count == 5){
                    dDay = "1~6차를 모두 접종하셨습니다.";
                    textViewdday.setText(dDay);
                    break;
                }
                dateArrayList.remove(0);
            }else if(compare < 0){
                if(nowDate.getMonth() != dateArrayList.get(0).getMonth()) {
                    if(dateArrayList.get(0).getMonth()==1||dateArrayList.get(0).getMonth()==3||dateArrayList.get(0).getMonth()==5||dateArrayList.get(0).getMonth()==7||
                            dateArrayList.get(0).getMonth()==8||dateArrayList.get(0).getMonth()==10||dateArrayList.get(0).getMonth()==12){
                        dDay = dateArrayList.get(0).getDate()+(31-nowDate.getDate())+"";
                        textViewdday.setText("접종날까지 D-"+dDay);
                        break;
                    }else if(dateArrayList.get(0).getMonth()==2){
                        dDay = dateArrayList.get(0).getDate()+(28-nowDate.getDate())+"";
                        textViewdday.setText("접종날까지 D-"+dDay);
                        break;
                    }else{
                        dDay = dateArrayList.get(0).getDate()+(30-nowDate.getDate())+"";
                        textViewdday.setText("접종날까지 D-"+dDay);
                        break;
                    }
                }
                dDay = dateArrayList.get(0).getDate()-nowDate.getDate()+"";
                textViewdday.setText("접종날까지 D-"+dDay);
                break;
            }
        }

    }
    public void dateCalculation(int count,Date date){ // 차수 계산
        events.removeAll(events);
        totalSchedule.removeAll(totalSchedule);
        String[] token = df.format(date).split("-");
        int year = Integer.parseInt(token[0]);
        int month = Integer.parseInt(token[1]);
        int day = Integer.parseInt(token[2]);

        events.add(getDate(year,month,day));

        int j = 14;
        for(int i = count+1;i<6;i++){
            totalSchedule.add( i+"차  "+df.format(getDate(year,month,day+j))+"\n");
            events.add(getDate(year,month,day+j));
            j +=14;
        }
        totalSchedule.add( 6+"차  "+df.format(getDate(year,month,day+j))+"\n");
        cv.updateCalendar(events);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);
        builder2 = new AlertDialog.Builder(this);
        events = new HashSet<>(); // 원하는 날짜에 마커


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("예방 접종일");
        actionBar.setDisplayHomeAsUpEnabled(true);

        cv = ((CalendarView)findViewById(R.id.calendar_view));
        cv.updateCalendar(events);

        textViewdday = (TextView) findViewById(R.id.dday);

        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler() // 달력 날짜 눌렀을때
        {

            @Override
            public void onDayLongPress(final Date date)
            {

/*                // show returned day
                Toast.makeText(VaccineActivity.this, df.format(date), Toast.LENGTH_SHORT).show();*/
                final CharSequence[] items = {"1차", "2차", "3차","4차","5차","6차"};
                // 여기서 부터는 알림창의 속성 설정
                builder2.setTitle("차수를 선택하세요")        // 제목 설
                        .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index){
                                //Toast.makeText(VaccineActivity.this, df.format(date)+"은 "+items[index]+"예방접종 날입니다.", Toast.LENGTH_SHORT).show();
                                dateCalculation(index,date);
                                dayCalculation(index,date);
                                dateUploadExecute(date,index);
                            }
                        });
                AlertDialog dialog = builder2.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기]
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
        dateUploadExecute(LocalDate.now().toDate(),-1);
    }

    private void showScheduleMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("전체일정");
        builder.setMessage(totalSchedule+"");

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


    @Override
    public void preTask() {

    }

    @Override
    public void postTask(Response response) {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            LocalDate date = LocalDate.parse("dd");
            String note = new String();

        } catch (JSONException | IOException e ) {
            e.printStackTrace();
            Toast.makeText(this, "서버 통신 오류", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelTask() {

    }

    @Override
    public void fairTask() {

    }


    private void dateUploadExecute(Date date, int count){
        FormBody.Builder builder= new FormBody.Builder()
                .add("month", DateTimeFormat.forPattern("YYYY-MM-dd").print(new LocalDate(date)));
        if(count != -1){
            builder.add("count",count+"");
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/animalMeal")
                .post(body)
                .build();
        new RequestTask(request,this,getApplicationContext()).execute();
    }


}
