package ourcompany.mylovepet.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import ourcompany.mylovepet.main.user.Pet;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

/**
 * Created by REOS on 2017-07-07.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, TaskListener{

    //플로팅 버튼 변수
    boolean isFloat = false;
    FloatingActionButton fButtonParent, fButtonAdd, fButtonDel, fButtonSet;
    ConstraintLayout floatingButtonLayout;

    //좌우 스크롤뷰
    ViewPager viewPager;

    //좌우 커서 이미지 아이콘 view
    View leftCursor, rightCursor;

    //펫 추가 액티비티 응답코드
    static final int SUCCESS_PET_ADD = 100;

    //AsyncTask 클래스
    RequestTask getPetsTask;

    String[] p = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,};

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        init(view);
        permissionSetting(p);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPetsExecute();
    }

    @Override
    public void onPause() {
        if(getPetsTask != null){
            getPetsTask.cancel(true);
        }
        super.onPause();
    }

    private void init(View view) {

        fButtonParent = (FloatingActionButton) view.findViewById(R.id.floatingButtonParent);
        fButtonAdd = (FloatingActionButton) view.findViewById(R.id.floatingButtonAdd);
        fButtonDel = (FloatingActionButton) view.findViewById(R.id.floatingButtonDel);
        fButtonSet = (FloatingActionButton) view.findViewById(R.id.floatingButtonSet);

        //프로팅 버튼 리스너 추가
        fButtonParent.setOnClickListener(this);
        fButtonAdd.setOnClickListener(this);
        fButtonDel.setOnClickListener(this);
        fButtonSet.setOnClickListener(this);

        //플로팅 버튼이 눌렸을때 화면에 투명도를 위해서 플로팅 버튼을 포함하는 레이아웃을 참조한다.
        floatingButtonLayout = (ConstraintLayout) view.findViewById(R.id.floatingButtonLayout);
        floatingButtonLayout.setClickable(false);
        floatingButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFloatingButton();
            }
        });

        leftCursor = view.findViewById(R.id.leftCursor);
        rightCursor = view.findViewById(R.id.rightCursor);

        //viewPager 참조
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
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
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.floatingButtonParent:
                floatingButton();
                break;
            case R.id.floatingButtonAdd:
                intent = new Intent(getContext(), PetRegistActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.floatingButtonDel:
                Pet[] pets = User.getIstance().getPets();
                int petNo = pets[viewPager.getCurrentItem()].getPetNo();
                break;
            case R.id.floatingButtonSet:
                intent = new Intent(getContext(), PetListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == SUCCESS_PET_ADD){
            getPetsExecute();
            return;
        }

    }

    //플로팅 버튼이 눌렀을떄의 동작
    private void floatingButton() {
        if (isFloat) {
            closeFloatingButton();
        } else {
            openFloatingButton();
        }
    }

    private void openFloatingButton() {
        floatingButtonLayout.setClickable(true);
        floatingButtonLayout.setBackgroundColor(Color.argb(220, 59, 59, 59));
        isFloat = true;
        fButtonAdd.setVisibility(View.VISIBLE);
        fButtonDel.setVisibility(View.VISIBLE);
        fButtonSet.setVisibility(View.VISIBLE);
    }

    private void closeFloatingButton() {
        floatingButtonLayout.setClickable(false);
        floatingButtonLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
        isFloat = false;
        fButtonAdd.setVisibility(View.INVISIBLE);
        fButtonDel.setVisibility(View.INVISIBLE);
        fButtonSet.setVisibility(View.INVISIBLE);

    }
    //플로팅 버튼이 눌렀을떄의 동작 끝


    //펫 정보 요청
    private void getPetsExecute(){
        RequestBody body= new FormBody.Builder().build();
        Request request = new Request.Builder()
<<<<<<< HEAD
                .addHeader("Cookie", User.getIstance().getCookie())
                .url("http://58.237.8.179/Servlet/animalInfo")
=======
                .url("http://58.226.2.45/Servlet/animalInfo")
>>>>>>> parent of 936c985... URL 클래스
                .post(body)
                .build();
        getPetsTask = new RequestTask(request,this,getContext().getApplicationContext());
        getPetsTask.execute();
    }
// 권한 되어있는지 요청 하여 없을 시 셋팅(최초 셋팅)
    public void permissionSetting(String[] permissionValues) {
               ActivityCompat.requestPermissions(getActivity(),permissionValues,1);
    }

    // TaskListener 메소드
    @Override
    public void preTask() { }

    @Override
    public void postTask(Response response) {
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
                viewPager.setAdapter(new PetInfoAdapter(getChildFragmentManager()));
                viewPager.setOffscreenPageLimit(pets.length);

                //펫이 2마리 이상이면 오른쪽 커서를 보이게 한다
                if (pets.length > 1){
                    rightCursor.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException | IOException e ) {
            e.printStackTrace();
            Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
        }finally {
            getPetsTask = null;
        }

    }

    @Override
    public void cancelTask() {

    }

    @Override
    public void fairTask() {

    }
    // TaskListener 메소드 end


    //유저의 펫 정보를 가져오고 AnimalInfoFragment클래스를 생성하여 viewPager에 등록
    private class GetPets extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        @Override
        public Response doInBackground(String... params) {
            RequestBody body= new FormBody.Builder().build();
            Request request = new Request.Builder()
                    .addHeader("Cookie", User.getIstance().getCookie())
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
                Toast.makeText(getContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
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
                    viewPager.setAdapter(new PetInfoAdapter(getChildFragmentManager()));
                    viewPager.setOffscreenPageLimit(pets.length);

                    //펫이 2마리 이상이면 오른쪽 커서를 보이게 한다
                    if (pets.length > 1){
                        rightCursor.setVisibility(View.VISIBLE);
                    }

                }

            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
