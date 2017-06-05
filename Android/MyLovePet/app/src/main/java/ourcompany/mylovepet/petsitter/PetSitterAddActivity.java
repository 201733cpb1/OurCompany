package ourcompany.mylovepet.petsitter;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;


import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.customView.PostDialog;

public class PetSitterAddActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    EditText post_editText;
    EditText s_DateEditText, e_DateEditText,totalDay;
    LocalDate s_Date, e_Date;
    DateTimeFormatter dateTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petsitter);
        toolbarInit();
        init();
    }

    private void toolbarInit(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        dtToggle = new ActionBarDrawerToggle(this,dlDrawer,toolbar,R.string.app_name,R.string.app_name);
        dlDrawer.addDrawerListener(dtToggle);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        dtToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);


        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sss),"홈") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sss),"통계") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sss),"펫등록") ;
    }

    private void init() {
        //주소 관련 뷰 찾기
        post_editText = (EditText)findViewById(R.id.post_editText);


        //날짜 관련 뷰 찾기
        s_DateEditText = (EditText) findViewById(R.id.input_sDate);
        e_DateEditText = (EditText) findViewById(R.id.input_eDate);
        totalDay = (EditText)findViewById(R.id.total_day);

        //타임 포맷 지정
        dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

        s_Date = LocalDate.now();
        e_Date = LocalDate.now();

        //현재 날짜로 설정
        s_DateEditText.setText(s_Date.toString(dateTimeFormat));
        e_DateEditText.setText(e_Date.toString(dateTimeFormat));

        //리스너 등록
        s_DateEditText.setOnClickListener(this);
        e_DateEditText.setOnClickListener(this);
        findViewById(R.id.findPost_button).setOnClickListener(this);
    }

    private void updateDate(){
        s_DateEditText.setText(s_Date.toString(dateTimeFormat));
        e_DateEditText.setText(e_Date.toString(dateTimeFormat));
        updateTotalDay();
    }

    private void updateTotalDay() {
        totalDay.setText((Days.daysBetween(s_Date,e_Date).getDays()+1)+"일");
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.input_sDate) {
            //다이얼 로그 시작
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    s_Date = LocalDate.fromCalendarFields(cal);

                    if(s_Date.isBefore(LocalDate.now())){
                        Toast.makeText(getApplicationContext(),"시작일이 현재 일보다 작을 수 없습니다.",Toast.LENGTH_SHORT).show();
                        s_Date = LocalDate.now();
                    }
                    if(e_Date.isBefore(s_Date)){
                        e_Date = s_Date.plusDays(1);
                    }
                    //날짜 수정
                    updateDate();
                }
            }, s_Date.getYear(), s_Date.getMonthOfYear() - 1, s_Date.getDayOfMonth()).show();
            //다이얼 로그 끝
        } else if (v.getId() == R.id.input_eDate) {
            //다이얼 로그 시작
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    e_Date = LocalDate.fromCalendarFields(cal);

                    if(e_Date.isBefore(s_Date)){
                        Toast.makeText(getApplicationContext(),"시작일보다 종료일이 작을 수 없습니다.",Toast.LENGTH_SHORT).show();
                        e_Date = s_Date.plusDays(1);
                    }
                    //날짜 수정
                    updateDate();
                }
            }, e_Date.getYear(), e_Date.getMonthOfYear() - 1, e_Date.getDayOfMonth()).show();
            //다이얼 로그 끝
        }else if(v.getId() == R.id.findPost_button){
            Dialog dialog = new PostDialog(this, new PostDialog.OnPostSetListener() {
                @Override
                public void onPostSet(final String zcode, final String address) {
                    post_editText.setText(address);
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        dlDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

