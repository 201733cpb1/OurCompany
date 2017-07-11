package ourcompany.mylovepet.main;


import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.daummap.MapActivity;
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

    Intent intent ;

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
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "홈"); //1
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "통계"); //2

        adapter.addItem("펫 시터");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "구하기"); //4
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "도움주기"); //5

        adapter.addItem("편의 기능");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "TIP"); //7
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "중고장터"); //8
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "탐색"); //9
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.walk), "SNS"); //10

        listview.setOnItemClickListener(this);

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
                break;
            case 8:
                //지름/중고장터 정보 화면 intro
                break;
            case 9:
/*                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);*/
                chkGpsService();
                break;
            case 10:
                //탐색 화면
                break;
            case 11:
                //SNS 화면
                break;
        }
        fragmentTransaction.commit();
        dlDrawer.closeDrawers();
    }
    public boolean chkGpsService() {

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
            intent = new Intent(this, MapActivity.class); //현재 위치 화면 띄우기 위해 인텐트 실행.
            startActivity(intent);
        }
        return false;
    }
}
