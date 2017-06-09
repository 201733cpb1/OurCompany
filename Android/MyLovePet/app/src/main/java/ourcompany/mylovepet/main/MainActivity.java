package ourcompany.mylovepet.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.customView.PetInfoAdapter;
import ourcompany.mylovepet.daummap.Intro;
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

    SwipeRefreshLayout swipeRefreshLayout;

    boolean isFloatedButton = false;
    FloatingActionButton fButtonParent, fButtonAdd, fButtonDel, fButtonSet;
    ConstraintLayout floatingButtonLayout;

    //좌우 스크롤뷰
    ViewPager viewPager;

    //포토
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CROP = 2;
    Uri photoURI, albumURI = null;
    Boolean album = false;
    final Context context = this;
    CircularImageView profile;
    String mCurrentPhotoPath;
    //포토 끝
    //
    GetPets taskGetPets;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getToken();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_Token", token);


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
                        chkGpsService();
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

        //viewPager 참조
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(10);

        updateData();
    }


    //상단 툴바에 새로고침 등록해서 아래 동작 정의..
    private void updateData() {
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
                startActivity(intent);
                break;
            case R.id.floatingButtonDel:
                Pet[] pets = User.getIstance().getPets();
                int petNo = pets[viewPager.getCurrentItem()].getPetNo();
                break;
            case R.id.floatingButtonSet:
                intent = new Intent(getApplicationContext(), PetUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.profile_picture:
                final CharSequence[] items = { "사진촬영", "갤러리","취소" };
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // 제목셋팅
                alertDialogBuilder.setTitle("선택해 주세요");
                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 프로그램을 종료한다
                        if(items[id]=="사진촬영"){
                            dispatchTakePictureIntent();
                        }else if(items[id]=="갤러리"){
                            doTakeAlbumAction();
                        }else{
                            dialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(),
                                items[id] + " 선택했습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // 다이얼로그 보여주기
                alertDialog.show();
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


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // 사진찍은 후 저장할 임시 파일//
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "createImageFile Failed", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                //photoURI = Uri.fromFile(photoFile); // 임시 파일의 위치,경로 가져옴
                photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // 임시 파일 위치에 저장
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException{
        String imageFileName = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory(),imageFileName);
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    private void doTakeAlbumAction() { // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "onActivityResult : RESULT_NOT_OK", Toast.LENGTH_LONG).show();
        } else {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: // 앨범 이미지 가져오기
                    album = true;
                    File albumFile = null;
                    try {
                        albumFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (albumFile != null) {
                        albumURI = Uri.fromFile(albumFile); // 앨범 이미지 Crop한 결과는 새로운 위치 저장
                    }

                    photoURI = data.getData(); // 앨범 이미지의 경로

                         /* profile에 띄우기*/
                    Bitmap image_bitmap = null;
                    try {
                        image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile.setImageBitmap(image_bitmap);
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    Bitmap image_bitmap2 = null;
                    try {
                        image_bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile.setImageBitmap(image_bitmap2);
                    break;
                    /*cropImage();
                    break;*/
                case REQUEST_IMAGE_CROP:
                    Bitmap photo = BitmapFactory.decodeFile(photoURI.getPath());
                    profile.setImageBitmap(photo);

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 동기화
                    if (album == false) {
                        mediaScanIntent.setData(photoURI); // 동기화
                    } else if (album == true) {
                        album = false;
                        mediaScanIntent.setData(albumURI); // 동기화
                    }
                    this.sendBroadcast(mediaScanIntent); // 동기화
                    break;
            }
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
            if(response == null | response.code() != 200) {
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
                            Pet pet = new Pet();
                            JSONObject object = jsonArray.getJSONObject(i);
                            pet.setPetNo(object.getInt("iAnimalNo"));
                            pet.setPetKind(object.getInt("iAnimalIndex"));
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
                    viewPager.setAdapter(PetInfoAdapter.createAdapter(getSupportFragmentManager()));
                }

            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
