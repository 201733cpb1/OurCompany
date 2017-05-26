package ourcompany.mylovepet.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by REOS on 2017-05-18.
 */

public class PetAddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{



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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        inIt();
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
                    new SerialNoCheck().execute();
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
                    new AddPet().execute();
                break;

        }
    }

    //스피너의 펫 종류 선택시 호출
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"Dd",Toast.LENGTH_SHORT).show();
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


    private class AddPet extends AsyncTask<String, Void, JSONObject> {

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "AnimalIndex="+-1+"&SerialNo="+strSerialNo+"&Name="+strPetName
                    +"&Gender="+strGender+"&Birth="+strBirth;
            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                //아이디 체크 url 적용
                String url = "http://58.237.8.179/Servlet/createAnimal";
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
            boolean isSuccessed = false;
            if(jsonObject == null)
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            else {
                try {
                    jsonObject = jsonObject.getJSONObject("CreateAnimalReulst");
                    isSuccessed = jsonObject.getBoolean("isSuccessed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(isSuccessed) {
                    Toast.makeText(getApplicationContext(), "펫 추가 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "펫 추가 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class SerialNoCheck extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            buttonSerial.setEnabled(false);
        }

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "serialNo="+strSerialNo;

            BufferedWriter writer = null;
            InputStream inputStream = null;

            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                //아이디 체크 url 적용/
                String url = "http://58.237.8.179/Servlet/checkSerial";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                //커넥션에 각종 정보 설정
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");


                writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

                writer.write(parameter);
                writer.flush();
                writer.close();


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
            buttonSerial.setEnabled(true);
            boolean isAble = false;
            if (jsonObject == null)
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            else {

                try {
                    jsonObject = jsonObject.getJSONObject("CheckResult");
                    isAble = jsonObject.getBoolean("isMattched");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isAble) {
                    nextPage();
                } else
                    Toast.makeText(getApplicationContext(), "사용 불가능한 시리얼 번호 입니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
