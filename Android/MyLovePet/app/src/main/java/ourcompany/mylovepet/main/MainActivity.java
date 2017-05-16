package ourcompany.mylovepet.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by REOS on 2017-05-07.
 */

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    TextView temp;
    TextView walk;
    TextView heartrate;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarInit();
        inIt();
    }

    private void toolbarInit() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.app_name, R.string.app_name);
        dlDrawer.addDrawerListener(dtToggle);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        dtToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);


        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sss), "홈");
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sss), "통계");
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.sss), "펫등록");
    }

    private void inIt() {
        temp = (TextView) findViewById(R.id.temp);
        walk = (TextView) findViewById(R.id.walk);
        heartrate = (TextView) findViewById(R.id.heartrate);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        updateData();
    }

    private void updateData() {
        new GetPets().execute();
    }

    @Override
    public void onRefresh() {
        updateData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private class GetCondition extends AsyncTask<String, Void, JSONObject> {
        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject responseJSON = null;
            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                String url = "http://58.237.8.179/Servlet/getCondition?editTextId=admin";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                //커넥션에 각종 정보 설정
                conn.setRequestMethod("GET");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "text/html");

                //응답 http코드를 가져옴
                int responseCode = conn.getResponseCode();

                ByteArrayOutputStream baos = null;
                InputStream is = null;
                String responseStr = null;

                //응답이 성공적으로 완료되었을 때
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    responseStr = new String(byteData);

                    responseJSON = new JSONObject(responseStr);


                } else {
                    is = conn.getErrorStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    responseStr = new String(byteData);
                    Log.i("info", "DATA response error msg = " + responseStr);
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.i("errorInfo", "error occured!" + e.getMessage());
            }

            return responseJSON;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


            if (jsonObject != null) {
                try {
                    jsonObject = jsonObject.getJSONObject("Condition");
                    temp.setText(jsonObject.getString("avgtemp"));
                    walk.setText(jsonObject.getString("step"));
                    heartrate.setText(jsonObject.getString("avgheart"));
                    Toast.makeText(getApplicationContext(), "업데이트 완료", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "업데이트 실패", Toast.LENGTH_SHORT).show();
            }

            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class GetPets extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            BufferedWriter writer = null;
            InputStream inputStream = null;
            JSONObject jsonObject = null;

            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                //아이디 체크 url 적용
                String url = "http://58.237.8.179/Servlet/animalInfo";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                //커넥션에 각종 정보 설정

                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Cookie",User.getIstance().getCookie());

                //응답 http코드를 가져옴
                int responseCode = conn.getResponseCode();

                inputStream = null;

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
            } else {
                JSONArray jsonArray = null;

                try {
                    jsonArray = jsonObject.getJSONArray("AnimalList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonArray != null) {
                    User user = User.getIstance();
                    int i2 = jsonArray.length();
                    Pet[] pets = new Pet[i2];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            Pet pet = new Pet();
                            JSONObject object = jsonArray.getJSONObject(0);
                            pet.setAnimalNo(object.getInt("iAnimalNo"));
                            pet.setAnimalIndex(object.getInt("iAnimalIndex"));
                            pet.setSerialNo(object.getInt("iSerialNo"));
                            pet.setName(object.getString("strName"));
                            pet.setGender(object.getString("strGender"));
                            pet.setBirth(object.getString("strBirth"));
                            pet.setPhoto_URL(object.getString("strPhoto"));
                            pets[i] = pet;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    user.setPets(pets);
                }

            }
        }
    }

}
