package ourcompany.mylovepet.temp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.daummap.Intro;
import ourcompany.mylovepet.main.PetAddActivity;
import ourcompany.mylovepet.main.PetInfoAdapter;
import ourcompany.mylovepet.main.PetListActivity;
import ourcompany.mylovepet.main.UserSettingActivity;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;
import ourcompany.mylovepet.market.Market_Intro;
import ourcompany.mylovepet.petsitter.PetSitterAddActivity;
import ourcompany.mylovepet.petsitter.PetSitterFindActivity;

/**
 * Created by REOS on 2017-05-07.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    String gpsEnabled;

    //플로팅 버튼 변수
    boolean isFloatedButton = false;
    FloatingActionButton fButtonParent, fButtonAdd, fButtonDel, fButtonSet;
    ConstraintLayout floatingButtonLayout;

    //좌우 스크롤뷰
    ViewPager viewPager;

    //좌우 커서 이미지 아이콘 view
    View leftCursor, rightCursor;


    //펫 추가 액티비티 응답코드
    static final int SUCCESS_PET_ADD = 100;


    //AsyncTask 클래스
    GetPets taskGetPets;

    String[] p = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getToken();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_Token", token);
        permissionSetting(p);

        toolbarInit();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskGetPets.cancel(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);

        adapter.addItem("펫 정보");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "홈"); //1
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "통계"); //2

        adapter.addItem("펫 시터");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "구하기"); //4
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "도움주기"); //5

        adapter.addItem("편의 기능");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "TIP"); //7
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "중고장터"); //8
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "탐색"); //9
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.walk), "SNS"); //10

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 1:
                        //홈화면
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case 2:
                        //통계 화면
                        break;
                    case 4:
                        //펫시터 구하기 화면
                        intent = new Intent(getApplicationContext(), PetSitterAddActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        //도움주기 화면
                        intent = new Intent(getApplicationContext(), PetSitterFindActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        //TIP 화면
                        break;
                    case 8:
                        //지름/중고장터 정보 화면 intro
                        intent = new Intent(getApplication(), Market_Intro.class);
                        startActivity(intent);
                        break;
                    case 9:
                        requestGPSPermission();
                        break;
                    case 10:
                        //탐색 화면
                        break;
                    case 11:
                        //SNS 화면
                        break;
                }
            }
        });
    }

    private void init() {

        taskGetPets = new GetPets();

        fButtonParent = (FloatingActionButton) findViewById(R.id.floatingButtonParent);
        fButtonAdd = (FloatingActionButton) findViewById(R.id.floatingButtonAdd);
        fButtonDel = (FloatingActionButton) findViewById(R.id.floatingButtonDel);
        fButtonSet = (FloatingActionButton) findViewById(R.id.floatingButtonSet);

        //프로팅 버튼 리스너 추가
        fButtonParent.setOnClickListener(this);
        fButtonAdd.setOnClickListener(this);
        fButtonDel.setOnClickListener(this);
        fButtonSet.setOnClickListener(this);

        //플로팅 버튼이 눌렸을때 화면에 투명도를 위해서 플로팅 버튼을 포함하는 레이아웃을 참조한다.
        floatingButtonLayout = (ConstraintLayout) findViewById(R.id.floatingButtonLayout);
        floatingButtonLayout.setClickable(false);
        floatingButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFloatingButton();
            }
        });

        leftCursor = findViewById(R.id.leftCursor);
        rightCursor = findViewById(R.id.rightCursor);

        //viewPager 참조
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageSize = viewPager.getAdapter().getCount();

                if(position == 0 ){
                    leftCursor.setVisibility(View.INVISIBLE);
                    rightCursor.setVisibility(View.VISIBLE);
                }else if(position == (pageSize-1)){
                    leftCursor.setVisibility(View.VISIBLE);
                    rightCursor.setVisibility(View.INVISIBLE);
                }else {
                    leftCursor.setVisibility(View.VISIBLE);
                    rightCursor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //펫 정보를 서버에서 가져온다.
        updateData();
    }


    //유저의 펫 정보를 서버에서 가져온다
    private void updateData() {
        taskGetPets = null;
        taskGetPets = new GetPets();
        taskGetPets.execute();
    }

    //플로팅 버튼이 눌렀을떄의 동작
    private void floatingButton() {
        if (isFloatedButton) {
            closeFloatingButton();
        } else {
            openFloatingButton();
        }
    }

    private void openFloatingButton() {
        floatingButtonLayout.setClickable(true);
        floatingButtonLayout.setBackgroundColor(Color.argb(220, 59, 59, 59));
        isFloatedButton = true;
        fButtonAdd.setVisibility(View.VISIBLE);
        fButtonDel.setVisibility(View.VISIBLE);
        fButtonSet.setVisibility(View.VISIBLE);
    }

    private void closeFloatingButton() {
        floatingButtonLayout.setClickable(false);
        floatingButtonLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
        isFloatedButton = false;
        fButtonAdd.setVisibility(View.INVISIBLE);
        fButtonDel.setVisibility(View.INVISIBLE);
        fButtonSet.setVisibility(View.INVISIBLE);
    }
    //플로팅 버튼이 눌렀을떄의 동작 끝


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.floatingButtonParent:
                floatingButton();
                break;
            case R.id.floatingButtonAdd:
                intent = new Intent(getApplicationContext(), PetAddActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.floatingButtonDel:
                Pet[] pets = User.getIstance().getPets();
                int petNo = pets[viewPager.getCurrentItem()].getPetNo();
                break;
            case R.id.floatingButtonSet:
                intent = new Intent(getApplicationContext(), PetListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(getApplicationContext(), UserSettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == SUCCESS_PET_ADD){
            updateData();
            return;
        }

    }


    private boolean chkGpsService() {

        //GPS가 켜져 있는지 확인함.
        gpsEnabled = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            //gps가 사용가능한 상태가 아니면
            new AlertDialog.Builder(this).setTitle("GPS 설정").setMessage("GPS가 꺼져 있습니다. \nGPS를 활성화 하시겠습니까?").setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    //GPS 설정 화면을 띄움
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();

        }else if((gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            Toast.makeText(getApplicationContext(), "정보를 읽어오는 중입니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Intro.class); //현재 위치 화면 띄우기 위해 인텐트 실행.
            startActivity(intent);
        }
        return false;
    }

    private void requestGPSPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }else {
            chkGpsService();
        }
    }

    // 권한 되어있는지 요청 하여 없을 시 셋팅(최초 셋팅)
    public void permissionSetting(String[] permissionValues) {
        ActivityCompat.requestPermissions(this,permissionValues,1);
    }


    //유저의 펫 정보를 가져오고 AnimalInfoFragment클래스를 생성하여 viewPager에 등록
    private class GetPets extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        @Override
        public Response doInBackground(String... params) {
            RequestBody body= new FormBody.Builder().build();
            Request request = new Request.Builder()
                    .addHeader("Cookie",User.getIstance().getCookie())
                    .url("http://58.237.8.179/Servlet/animalInfo")
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
            if(response == null || response.code() != 200) {
                Toast.makeText(getApplicationContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray;
                jsonArray = jsonObject.getJSONArray("AnimalList");

                if (jsonArray != null) {
                    User user = User.getIstance();
                    int length = jsonArray.length();
                    Pet[] pets = new Pet[length];
                    for (int i = 0; i < length; i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Pet.Builder builder = new Pet.Builder(object.getInt("iAnimalNo"));
                            builder.petKind(object.getInt("iAnimalIndex"))
                                    .serialNo(object.getInt("iSerialNo"))
                                    .name(object.getString("strName"))
                                    .gender(object.getString("strGender"))
                                    .birth(object.getString("strBirth"))
                                    .photo_URL(object.getString("strPhoto"));
                            Pet pet = builder.build();
                            pets[i] = pet;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    user.setPets(pets);
                    viewPager.setAdapter(new PetInfoAdapter(getSupportFragmentManager()));
                    viewPager.setOffscreenPageLimit(pets.length);

                    //펫이 2마리 이상이면 오른쪽 커서를 보이게 한다
                    if (pets.length > 1){
                        rightCursor.setVisibility(View.VISIBLE);
                    }

                }

            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }


        }

    }

}
