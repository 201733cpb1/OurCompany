﻿package ourcompany.mylovepet.main;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ourcompany.mylovepet.R;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
import ourcompany.mylovepet.WebViewFragment;
import ourcompany.mylovepet.board.TipBoardFragment;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.daummap.GpsMapActivity;
import ourcompany.mylovepet.daummap.Intro;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.petsitter.PetSitterFindFragment;
import ourcompany.mylovepet.petsitter.SitterRegisterFragment;

/**
 * Created by REOS on 2017-07-07.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    FragmentManager fragmentManager;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

    Intent intent;
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
    OnBackKeyPressListener onBackKeyPressListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarInit();

        //홈화면으로 시작
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        findViewById(R.id.myInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.container,new MyPageFragment()).commit();
                dlDrawer.closeDrawers();
            }
        });

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
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.home), "홈"); //1
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.statistic), "통계"); //2

        adapter.addItem("펫 시터");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "구하기"); //4
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.petsitter), "도움주기"); //5

        adapter.addItem("편의 기능");
<<<<<<< HEAD

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tip), "TIP"); //7
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.used), "중고장터"); //8
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.searching), "탐색"); //9
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.festival), "SNS"); //10


=======
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.tip), "TIP"); //6
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.used), "중고장터"); //7
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.searching), "탐색"); //8
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.festival), "SNS"); //9

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
        listview.setOnItemClickListener(this);

        ((TextView)findViewById(R.id.nickName)).setText(User.getIstance().getSunName() + " 님");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        switch (position) {
            case 1:
                //홈화면
                fragmentTransaction.replace(R.id.container,new HomeFragment());
                break;
            case 2:
                //통계 화면
                break;
<<<<<<< HEAD

            case 4:
                //펫시터 구하기 화면
                fragmentTransaction.replace(R.id.container,new SitterRegisterFragment());
                break;
            case 5:
                //도움주기 화면
                fragmentTransaction.replace(R.id.container, new PetSitterFindFragment());
                break;
            case 7:
                //TIP 화면
=======
            case 4: //도움주기 화면
                webViewFragment =  WebViewFragment.createWebViewFragment("http://58.226.2.45/petSitter?native=android");
                fragmentTransaction.replace(R.id.container, webViewFragment);
                break;
            case 6: //TIP 화면
                webViewFragment =  WebViewFragment.createWebViewFragment("http://58.226.2.45/tip?native=android");
                fragmentTransaction.replace(R.id.container, webViewFragment);
                break;
            case 7:
                webViewFragment = WebViewFragment.createWebViewFragment("http://58.226.2.45/market?native=android");
                fragmentTransaction.replace(R.id.container, webViewFragment);
                //지름/중고장터 정보 화면 intro
>>>>>>> parent of 936c985... URL 클래스
                break;
            case 8:
                fragmentTransaction.replace(R.id.container, new WebViewTest());
                //지름/중고장터 정보 화면 intro

                break;
            case 9:
                chkGpsService();  //탐색 화면
                break;
            case 10:
                intent = new Intent(this, GpsMapActivity.class); //현재 위치 화면 띄우기 위해 인텐트 실행.
                startActivity(intent);
                //SNS 화면
                break;
            case 11:
                break;
        }
        fragmentTransaction.commit();
        dlDrawer.closeDrawers();
    }

    public boolean chkGpsService() {
        Intent intent ;
        //GPS가 켜져 있는지 확인함.
        String gpsEnabled = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

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
            intent = new Intent(this, Intro.class); //현재 위치 화면 띄우기 위해 인텐트 실행.
            startActivity(intent);
        }
        return false;
    }
}

