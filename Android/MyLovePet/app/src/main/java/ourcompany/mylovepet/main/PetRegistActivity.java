package ourcompany.mylovepet.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

/**
 * Created by REOS on 2017-05-18.
 */

public class PetRegistActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    enum Position{
        POS_SERIAL_NO, POS_PET_NAME, POS_BIRTH, POS_GENDER,POS_TYPE;
        public static Position getPosition(int pos){
            if(pos == 0)
                return POS_SERIAL_NO;
            else if(pos == 1)
                return POS_PET_NAME;
            else if(pos == 2)
                return POS_BIRTH;
            else if(pos == 3)
                return POS_GENDER;
            else
                return POS_TYPE;
        }
    }

    // View layoutPetName, layoutBirth, layoutGender, layoutType, layoutSerialNo;
    View[] layoutList;
    EditText editTextPetName,editTextSerialNo;
    DatePicker datePickerBirth;
    Spinner spinnerType1, spinnerType2;
    Button buttonSerial,buttonMan, buttonWoman, buttonGender;
    String strPetName,strBirth,strGender,strType,strSerialNo;
    Position nowPos;

    SpinnerAdapter spinnerAdapterType, spinnerAdapterDog, spinnerAdapterCat;

    TaskListener serialNoTask;
    TaskListener petRegisterTask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        inIt();
        listenerInit();
    }

    private void inIt(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("동물 추가");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutList = new View[5];
        layoutList[0] = findViewById(R.id.layoutSerialNo);
        layoutList[1] = findViewById(R.id.layoutPetName);
        layoutList[2] = findViewById(R.id.layoutBirth);
        layoutList[3] = findViewById(R.id.layoutGender);
        layoutList[4] = findViewById(R.id.layoutType);


        layoutList[0].setVisibility(View.VISIBLE);
        layoutList[1].setVisibility(View.INVISIBLE);
        layoutList[2].setVisibility(View.INVISIBLE);
        layoutList[3].setVisibility(View.INVISIBLE);
        layoutList[4].setVisibility(View.INVISIBLE);


        buttonSerial = (Button)findViewById(R.id.buttonSerialNo);
        buttonSerial.setOnClickListener(this);

        buttonGender = (Button)findViewById(R.id.buttonGender);
        buttonGender.setEnabled(false);
        buttonGender.setOnClickListener(this);

        findViewById(R.id.buttonPetName).setOnClickListener(this);
        findViewById(R.id.buttonBirth).setOnClickListener(this);
        findViewById(R.id.buttonType).setOnClickListener(this);


        buttonMan = (Button)findViewById(R.id.buttonMan);
        buttonWoman = (Button)findViewById(R.id.buttonWoman);
        buttonMan.setOnClickListener(this);
        buttonWoman.setOnClickListener(this);

        editTextPetName = (EditText)findViewById(R.id.editTextPetName);
        editTextSerialNo = (EditText)findViewById(R.id.editTextSerialNo);
        datePickerBirth = (DatePicker)findViewById(R.id.datePickerBirth);


        //동물 타입 스피너 설정
        spinnerType1 = (Spinner)findViewById(R.id.spinerType1);
        spinnerType2 = (Spinner)findViewById(R.id.spinerType2);


        spinnerAdapterType = ArrayAdapter.createFromResource(this,R.array.pets,android.R.layout.simple_spinner_item);
        spinnerAdapterDog = ArrayAdapter.createFromResource(this,R.array.dog,android.R.layout.simple_spinner_item);
        spinnerAdapterCat = ArrayAdapter.createFromResource(this,R.array.cat,android.R.layout.simple_spinner_item);

        spinnerType1.setAdapter(spinnerAdapterType);

        spinnerType1.setOnItemSelectedListener(this);
        spinnerType2.setOnItemSelectedListener(this);


        //동물 타입 스피너 설정 끝

        //초기 페이지 설정
        nowPos = Position.POS_SERIAL_NO;
    }

    private void listenerInit(){
        serialNoTask = new TaskListener() {
            @Override
            public void preTask() {
                buttonSerial.setEnabled(false);
            }

            @Override
            public void postTask(Response response) {
                try {
                    boolean isAble = false;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject = jsonObject.getJSONObject("CheckResult");
                    isAble = jsonObject.getBoolean("isMattched");
                    if (isAble) {
                        nextPage();
                    } else
                        Toast.makeText(getApplicationContext(), "사용 불가능한 시리얼 번호 입니다.", Toast.LENGTH_SHORT).show();

                } catch (JSONException | IOException e ) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }finally {
                    buttonSerial.setEnabled(true);
                }
            }

            @Override
            public void cancelTask() {

            }

            @Override
            public void fairTask() {

            }
        };

        petRegisterTask = new TaskListener() {
            @Override
            public void preTask() {

            }

            @Override
            public void postTask(Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject = jsonObject.getJSONObject("CreateAnimalReulst");
                    boolean isSuccessed;
                    isSuccessed = jsonObject.getBoolean("isSuccessed");
                    if(isSuccessed) {
                        Toast.makeText(getApplicationContext(), "펫 추가 완료", Toast.LENGTH_SHORT).show();
                        setResult(HomeFragment.SUCCESS_PET_ADD);
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "펫 추가 실패", Toast.LENGTH_SHORT).show();

                } catch (JSONException | IOException e ) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancelTask() {

            }

            @Override
            public void fairTask() {

            }
        };

    }


    //서버 통신 메소드
    private void serialNoExecute(){
        RequestBody body= new FormBody.Builder().add("serialNo",strSerialNo).build();
        Request request = new Request.Builder()
<<<<<<< HEAD
                .url("http://58.237.8.179/Servlet/checkSerial")
=======
                .url("http://58.226.2.45/Servlet/checkSerial")
>>>>>>> parent of 936c985... URL 클래스
                .post(body)
                .build();
        new RequestTask(request,serialNoTask,getApplicationContext()).execute();
    }

    private void petRegisterExecute(){
        RequestBody body= new FormBody.Builder()
                .add("AnimalIndex",-1+"")
                .add("SerialNo", strSerialNo)
                .add("Name", strPetName)
                .add("Gender", strGender)
                .add("Birth", strBirth)
                .build();

        Request request = new Request.Builder()
<<<<<<< HEAD
                .addHeader("Cookie",User.getIstance().getCookie())
                .url("http://58.237.8.179/Servlet/createAnimal")
=======
                .url("http://58.226.2.45/Servlet/createAnimal")
>>>>>>> parent of 936c985... URL 클래스
                .post(body)
                .build();
        new RequestTask(request,petRegisterTask,getApplicationContext()).execute();
    }
    //서버 통신 메소드

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId){
            case R.id.buttonMan:
                buttonGender.setEnabled(true);
                buttonMan.setEnabled(false);
                buttonWoman.setEnabled(true);
                strGender = "남";
                break;
            case R.id.buttonWoman:
                buttonGender.setEnabled(true);
                buttonMan.setEnabled(true);
                buttonWoman.setEnabled(false);
                strGender = "여";
                break;
            case R.id.buttonSerialNo:
                strSerialNo = editTextSerialNo.getText().toString();
                if(isNotNull(strSerialNo,viewId)){
                    serialNoExecute();
                }
                break;
            case R.id.buttonPetName:
                strPetName = editTextPetName.getText().toString();
                if(isNotNull(strPetName,viewId)){
                    nextPage();
                }
                break;
            case R.id.buttonBirth:
                String year = datePickerBirth.getYear()+"";
                String month = (datePickerBirth.getMonth()+1)+"";
                String day = datePickerBirth.getDayOfMonth()+"";
                strBirth = year+"-"+month+"-"+day;
                nextPage();
                break;
            case R.id.buttonGender:
                nextPage();
                break;
            case R.id.buttonType:
                if(isNotNull(strType,viewId))
                    petRegisterExecute();
                break;

        }
    }

    //스피너의 펫 종류 선택시 호출
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == spinnerAdapterType){
            if(position == 0){
                spinnerType2.setAdapter(spinnerAdapterDog);
            }else{
                spinnerType2.setAdapter(spinnerAdapterCat);
            }
        }else {
            if(parent.getAdapter() == spinnerAdapterDog){
                strType = Integer.toString(position);
            }
            else {
                strType = Integer.toString(position+1000);
            }
        }

    }
    //스피너의 펫 종류 선택시 호출
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //툴바 버튼 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //다음 페이지 처리
    private void nextPage(){
        layoutList[nowPos.ordinal()].setVisibility(View.INVISIBLE);
        layoutList[nowPos.ordinal()+1].setVisibility(View.VISIBLE);
        nowPos = Position.getPosition(nowPos.ordinal()+1);
    }

    //뒤로가기 버튼 눌렀을때 처리
    @Override
    public void onBackPressed() {
        if(nowPos == Position.POS_SERIAL_NO)
            finish();
        else {
            layoutList[nowPos.ordinal()].setVisibility(View.INVISIBLE);
            layoutList[nowPos.ordinal()-1].setVisibility(View.VISIBLE);
            nowPos = Position.getPosition(nowPos.ordinal()-1);
        }
    }

    private boolean isNotNull(String str, int id) {
        if (str.equals("")) {
            switch (id) {
                case R.id.buttonPetName:
                    Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonSerialNo:
                    Toast.makeText(this, "시리얼번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonType:
                    Toast.makeText(this, "펫의 종류를 선택해주세요", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        } else {
            return true;
        }

    }


    private class AddPet extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        @Override
        public Response doInBackground(String... params) {
            RequestBody body= new FormBody.Builder()
                    .add("AnimalIndex",-1+"")
                    .add("SerialNo", strSerialNo)
                    .add("Name", strPetName)
                    .add("Gender", strGender)
                    .add("Birth", strBirth)
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Cookie",User.getIstance().getCookie())
                    .url("http://58.237.8.179/Servlet/createAnimal")
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
                jsonObject = jsonObject.getJSONObject("CreateAnimalReulst");
                boolean isSuccessed;
                isSuccessed = jsonObject.getBoolean("isSuccessed");
                if(isSuccessed) {
                    Toast.makeText(getApplicationContext(), "펫 추가 완료", Toast.LENGTH_SHORT).show();
                    setResult(HomeFragment.SUCCESS_PET_ADD);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "펫 추가 실패", Toast.LENGTH_SHORT).show();

            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private class SerialNoCheck extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            buttonSerial.setEnabled(false);
        }

        @Override
        public Response doInBackground(String... params) {
            RequestBody body= new FormBody.Builder().add("serialNo",strSerialNo).build();
            Request request = new Request.Builder()
                    .url("http://58.237.8.179/Servlet/checkSerial")
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
            buttonSerial.setEnabled(true);
            if(response == null || response.code() != 200) {
                Toast.makeText(getApplicationContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                boolean isAble = false;
                JSONObject jsonObject = new JSONObject(response.body().string());
                jsonObject = jsonObject.getJSONObject("CheckResult");
                isAble = jsonObject.getBoolean("isMattched");
                if (isAble) {
                    nextPage();
                } else
                    Toast.makeText(getApplicationContext(), "사용 불가능한 시리얼 번호 입니다.", Toast.LENGTH_SHORT).show();

            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
