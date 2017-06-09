package ourcompany.mylovepet.petsitter;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

public class PetSitterAddActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    EditText editTextPetCount,editTextBody,editTextTitle;
    EditText s_DateEditText, e_DateEditText,totalDay;
    LocalDate s_Date, e_Date;
    DateTimeFormatter dateTimeFormat;

    ViewPager viewPager;

    HashSet<Integer> petNoSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petsitter_add);
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
        //펫 카운트 뷰 찾기
        editTextPetCount = (EditText)findViewById(R.id.animal_count);
        editTextBody = (EditText)findViewById(R.id.editTextBody);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);

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
        //findViewById(R.id.findPost_button).setOnClickListener(this);
        findViewById(R.id.buttonAddBoard).setOnClickListener(this);

        viewPager = (ViewPager)findViewById(R.id.viewPagerPetList);

        Pet[] pets = User.getIstance().getPets();

        viewPager.setAdapter(new PetViewPager(getLayoutInflater(), pets));
        viewPager.setOffscreenPageLimit(pets.length);

        petNoSet = new HashSet<>();

    }

    private void updateDate(){
        s_DateEditText.setText(s_Date.toString(dateTimeFormat));
        e_DateEditText.setText(e_Date.toString(dateTimeFormat));
        updateTotalDay();
    }

    private void updateTotalDay() {
        totalDay.setText((Days.daysBetween(s_Date,e_Date).getDays()+1)+"일");
    }

    private void updatePetCount(){
        int size = petNoSet.size();
        editTextPetCount.setText(size+" 마리");
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
        }else if (v.getId() == R.id.buttonAddBoard){
            if(petNoSet.size() == 0){
                Toast.makeText(getApplicationContext(),"펫을 추가 해주세요",Toast.LENGTH_SHORT).show();
            }else {
                new AddPetSitter().execute();
            }
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        dlDrawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class AddPetSitter1 extends AsyncTask<String, Void, JSONObject> {

        String strSDate,strEDate,strBody, strTitle;

        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            strSDate = s_DateEditText.getText().toString();
            strEDate = e_DateEditText.getText().toString();
            strTitle = editTextTitle.getText().toString();
            strBody = editTextBody.getText().toString();

            jsonArray = new JSONArray();

            for(int no : petNoSet){
                jsonArray.put(no);
            }
        }

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "Date="+strSDate+"&Term="+strEDate+
                    "&Title="+ strTitle +"&Feedback="+strBody+"&petList="+jsonArray.toString();
            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                String url = "http://58.237.8.179/Servlet/addPetsitter";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                //커넥션에 각종 정보 설정
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Cookie", User.getIstance().getCookie());


                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                writer.write(parameter);
                writer.flush();
                writer.close();

                //응답 http코드를 가져옴
                int responseCode = conn.getResponseCode();

                InputStream inputStream = null;

                //응답이 성공적으로 완료되었을 때
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    StringBuilder strBuffer = new StringBuilder();
                    while ((str = bufferedReader.readLine()) != null) {
                        strBuffer.append(str);
                    }
                    jsonObject = new JSONObject(strBuffer.toString());
                    inputStream.close();
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("errorInfo", "error occured!" + e.getMessage());
            }

            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject == null) {
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                return;
            }


        }

    }



    private class AddPetSitter extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        String strSDate,strEDate,strBody, strTitle;

        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            strSDate = s_DateEditText.getText().toString();
            strEDate = e_DateEditText.getText().toString();
            strTitle = editTextTitle.getText().toString();
            strBody = editTextBody.getText().toString();

            jsonArray = new JSONArray();

            for(int no : petNoSet){
                jsonArray.put(no);
            }
        }

        @Override
        public Response doInBackground(String... params) {
            RequestBody body= new FormBody.Builder()
                    .add("Date", strSDate)
                    .add("Term", strEDate)
                    .add("Title", strTitle)
                    .add("Feedback", strBody)
                    .add("petList", jsonArray.toString())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie",User.getIstance().getCookie())
                    .url("http://58.237.8.179/Servlet/addPetsitter")
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            if(response == null | response.code() != 200) {
                Toast.makeText(getApplicationContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                jsonObject = jsonObject.getJSONObject("AddPetSitter");
                boolean isSuccessed = false;
                isSuccessed = jsonObject.getBoolean("isSuccessed");

                if(isSuccessed){
                    Toast.makeText(getApplicationContext(),"글 등록 완료",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"등록 실패",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class PetViewPager extends PagerAdapter{

        Pet[] pets;
        LayoutInflater inflater;

        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                petNoSet.add(pets[position].getPetNo());
                ((Button)v).setText("취소");
                v.setOnClickListener(deleteListener);
                Log.d("테스트",petNoSet.toString());
                updatePetCount();
            }
        };

        View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                petNoSet.remove(pets[position].getPetNo());
                ((Button)v).setText("추가");
                v.setOnClickListener(addListener);
                Log.d("테스트",petNoSet.toString());
                updatePetCount();
            }
        };


        public PetViewPager(LayoutInflater inflater, Pet[] pets){
            this.pets = pets;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return pets.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = inflater.inflate(R.layout.pet,container,false);

            TextView textViewPetName = (TextView)view.findViewById(R.id.textViewPetName);
            textViewPetName.setText(pets[position].getName());

            view.findViewById(R.id.buttonAdd).setOnClickListener(addListener);

            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

}

