package ourcompany.mylovepet.main;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.Pet;
import ourcompany.mylovepet.main.user.PetManager;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by REOS on 2017-07-07.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnBackKeyPressListener {

    //플로팅 버튼 변수
    boolean isFloat = false;
    FloatingActionButton fButtonParent, fButtonAdd, fButtonDel, fButtonSet;
    ConstraintLayout floatingButtonLayout;

    //좌우 스크롤뷰
    ViewPager viewPager;

    //좌우 커서 이미지 아이콘 view
    View leftCursor, rightCursor;


    //AsyncTask 클래스
    ServerTaskManager getPetsTask;

    TaskListener getPetsTaskListener;

    PetManager petManager;


    public HomeFragment() {
        petManager = User.getIstance().getPetManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);
        listenerInit();
        permissionSetting();

        return view;
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
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageSize = viewPager.getAdapter().getCount();

                if (position == 0) {
                    leftCursor.setVisibility(View.INVISIBLE);
                    rightCursor.setVisibility(View.VISIBLE);
                } else if (position == (pageSize - 1)) {
                    leftCursor.setVisibility(View.VISIBLE);
                    rightCursor.setVisibility(View.INVISIBLE);
                } else {
                    leftCursor.setVisibility(View.VISIBLE);
                    rightCursor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void listenerInit() {
        getPetsTaskListener = new TaskListener() {
            // TaskListener 메소드
            @Override
            public void preTask() {
            }

            @Override
            public void postTask(byte[] bytes) {
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray jsonArray = jsonObject.getJSONArray("AnimalList");
                    if (jsonArray != null) {
                        petManager = User.getIstance().getPetManager();
                        petManager.clearPet();
                        int length = jsonArray.length();
                        Pet[] pets = new Pet[length];
                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Pet pet = new Pet.Builder(object.getInt("iAnimalNo"))
                                    .petKind(object.getString("iAnimalKind"))
                                    .serialNo(object.getInt("iSerialNo"))
                                    .name(object.getString("strName"))
                                    .gender(object.getString("strGender"))
                                    .birth(object.getString("strBirth"))
                                    .photoFileNo(object.getString("strPhoto"))
                                    .lastMealDate(object.getString("lastMeal"))
                                    .walkCount(object.getInt("walkCount"))
                                    .build();
                            pets[i] = pet;
                        }
                        petManager.setPets(pets);
                        viewPager.setAdapter(new PetInfoFragmentAdapter(getChildFragmentManager()));
                        viewPager.setOffscreenPageLimit(pets.length);
                        //펫이 2마리 이상이면 오른쪽 커서를 보이게 한다
                        if (pets.length > 1) {
                            leftCursor.setVisibility(View.INVISIBLE);
                            rightCursor.setVisibility(View.VISIBLE);
                        } else {
                            rightCursor.setVisibility(View.INVISIBLE);
                            leftCursor.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                } finally {
                    getPetsTask = null;
                }
            }

            @Override
            public void fairTask() {
                getPetsTask = null;
            }
            // TaskListener 메소드 end

        };
    }

    @Override
    public void onStart() {
        getPetsExecute();
        ((MainActivity) getActivity()).setOnBackKeyPressListener(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        if (getPetsTask != null) {
            getPetsTask.cancel(true);
        }
        ((MainActivity) getActivity()).setOnBackKeyPressListener(null);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.floatingButtonParent:
                onClickFloatingButton();
                break;
            case R.id.floatingButtonAdd:
                intent = new Intent(getContext(), PetRegistActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.floatingButtonDel:
                Pet pet = petManager.getPet(viewPager.getCurrentItem());
                Dialog dialog = new PetDeleteDialog(getContext(), pet);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getPetsExecute();
                    }
                });
                dialog.show();
                closeFloatingButton();
                break;
            case R.id.floatingButtonSet:
                intent = new Intent(getContext(), PetListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getPetsExecute();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //플로팅 버튼이 눌렀을떄의 동작
    private void onClickFloatingButton() {
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
    protected void getPetsExecute() {
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url("http://58.226.2.45/Servlet/animalInfo")
                .post(body)
                .build();
        getPetsTask = new ServerTaskManager(request, getPetsTaskListener, getContext().getApplicationContext());
        getPetsTask.execute();
    }

    // 권한 되어있는지 요청 하여 없을 시 셋팅(최초 셋팅)
    public void permissionSetting() {
        String[] permissionValues = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};

        ActivityCompat.requestPermissions(getActivity(), permissionValues, 1);
    }

    // 액티비티가 받는 뒤로가기 이벤트를 받기위한 인터페이스
    @Override
    public boolean onBack() {
        if (isFloat) {
            closeFloatingButton();
            return true;
        } else {
            return false;
        }
    }

}
