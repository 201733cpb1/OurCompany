package ourcompany.mylovepet.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
import java.util.regex.Pattern;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.PostDialog;

/**
 * Created by REOS on 2017-05-08.
 */

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{

    private enum Position {
        POS_ID(0), POS_PASSWORD(1), POS_PASSWORD_CHECK(2), POS_EMAIL(3), POS_NAME(4), POS_SUB_NAME(5), POS_ADDRESS(6);
        private final int value;
        Position(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public static Position back(Position position){
            switch (position){
                case POS_ADDRESS:
                    return POS_SUB_NAME;
                case POS_SUB_NAME:
                    return POS_NAME;
                case POS_NAME:
                    return POS_EMAIL;
                case POS_EMAIL:
                    return POS_PASSWORD_CHECK;
                case POS_PASSWORD_CHECK:
                    return POS_PASSWORD;
                case POS_PASSWORD:
                    return POS_ID;
                default:
                    return POS_ID;
            }
        }
    }

    PostDialog.OnPostSetListener onPostSetListener;

    private Position nowPos;

    private View[] viewList;
    private View layoutId, layoutPassword, layoutPasswordCheck, layoutEmail, layoutName, layoutSubName, layoutAddress;
    private EditText editTextId, editTextPassword, editTextPasswordCheck, editTextEmail, editTextName, editTextSubName, editTextAddress,editTextAddress2;
    private String strId, strPassword, strPasswordCheck, strEmail, strName, strSubName, strAddress,strAddress2,strZoneCode;
    private Button buttonId, buttonSubName;

    Pattern idFilter;
    Pattern passwordFilter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        inIt();
    }


    private void inIt() {

        idFilter = Pattern.compile("^[a-zA-Z0-9]*$");
        passwordFilter = Pattern.compile("^[!@#$%^&*()a-zA-Z0-9]*$");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("아이디");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutId = findViewById(R.id.layoutId);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutPasswordCheck = findViewById(R.id.layoutPasswordCheck);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutName = findViewById(R.id.layoutName);
        layoutSubName = findViewById(R.id.layoutSubName);
        layoutAddress = findViewById(R.id.layoutAddress);

        viewList = new View[7];
        viewList[0] = layoutId;
        viewList[1] = layoutPassword;
        viewList[2] = layoutPasswordCheck;
        viewList[3] = layoutEmail;
        viewList[4] = layoutName;
        viewList[5] = layoutSubName;
        viewList[6] = layoutAddress;

        layoutId.setVisibility(View.VISIBLE);
        layoutPassword.setVisibility(View.INVISIBLE);
        layoutPasswordCheck.setVisibility(View.INVISIBLE);
        layoutEmail.setVisibility(View.INVISIBLE);
        layoutName.setVisibility(View.INVISIBLE);
        layoutSubName.setVisibility(View.INVISIBLE);
        layoutAddress.setVisibility(View.INVISIBLE);

        nowPos = Position.POS_ID;

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordCheck = (EditText) findViewById(R.id.editTextPasswordCheck);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSubName = (EditText) findViewById(R.id.editTextSubName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextAddress2 = (EditText) findViewById(R.id.editTextAddress2);


        buttonId = (Button) findViewById(R.id.buttonId);
        buttonId.setOnClickListener(this);
        findViewById(R.id.buttonPassword).setOnClickListener(this);
        findViewById(R.id.buttonPasswordCheck).setOnClickListener(this);
        findViewById(R.id.buttonEmail).setOnClickListener(this);
        findViewById(R.id.buttonName).setOnClickListener(this);
        buttonSubName = (Button) findViewById(R.id.buttonSubName);
        buttonSubName.setOnClickListener(this);
        findViewById(R.id.buttonAddress).setOnClickListener(this);


        onPostSetListener = new PostDialog.OnPostSetListener() {
            @Override
            public void onPostSet(String zoneCode, String address) {
                strZoneCode = zoneCode;
                editTextAddress.setText(address);
            }
        };

        findViewById(R.id.buttonPostSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new PostDialog(JoinActivity.this,onPostSetListener).show();
            }
        });


    }

    private boolean isNotNull(String str, int id) {
        if (str.equals("")) {
            switch (id) {
                case R.id.buttonId:
                    Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonPassword:
                    Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonPasswordCheck:
                    Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonEmail:
                    Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonName:
                    Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonSubName:
                    Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonAddress:
                    Toast.makeText(this, "주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.buttonId:
                //아이디 가져오기
                strId = editTextId.getText().toString();
                //입력이 되었는지 확인
                if (isNotNull(strId, viewId)) {
                    //아이디 형식이 맞는지 확인
                    if (!idFilter.matcher(strId).matches())
                        Toast.makeText(this, "잘못된 입력", Toast.LENGTH_SHORT).show();
                    else {
                        new IdCheck().execute(strId);
                    }
                    //아이디 형식이 맞는지 확인 끝
                }
                //입력이 되었는지 확인 끝
                break;
            case R.id.buttonPassword:
                //패스워드 가져오기
                strPassword = editTextPassword.getText().toString();
                //입력이 되었는지 확인
                if (isNotNull(strPassword, viewId)) {
                    //비밀번호 형식이 맞는지 확인
                    if (!passwordFilter.matcher(strPassword).matches())
                        Toast.makeText(this, "잘못된 입력", Toast.LENGTH_SHORT).show();
                    else {
                        layoutPassword.setVisibility(View.INVISIBLE);
                        layoutPasswordCheck.setVisibility(View.VISIBLE);
                        nowPos = Position.POS_PASSWORD_CHECK;
                    }
                    //비밀번호 형식이 맞는지 확인 끝
                }
                //입력이 되었는지 끝
                break;
            case R.id.buttonPasswordCheck:
                strPasswordCheck = editTextPasswordCheck.getText().toString();
                if (isNotNull(strPasswordCheck, viewId)) {
                    //비밀번호를 비교해서 사용자가 원하는 비밀번호를 정확히 입력했는지 확인
                    if (strPassword.equals(strPasswordCheck)) {
                        layoutPasswordCheck.setVisibility(View.INVISIBLE);
                        layoutEmail.setVisibility(View.VISIBLE);
                        nowPos = Position.POS_EMAIL;
                    }
                    else Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    //비밀번호를 비교해서 사용자가 원하는 비밀번호를 정확히 입력했는지 확인 끝
                }
                break;
            case R.id.buttonEmail:
                strEmail = editTextEmail.getText().toString();
                if (isNotNull(strEmail, viewId)) {
                    layoutEmail.setVisibility(View.INVISIBLE);
                    layoutName.setVisibility(View.VISIBLE);
                    nowPos = Position.POS_NAME;
                }
                break;
            case R.id.buttonName:
                strName = editTextName.getText().toString();
                if (isNotNull(strName, viewId)) {
                    layoutName.setVisibility(View.INVISIBLE);
                    layoutSubName.setVisibility(View.VISIBLE);
                    nowPos = Position.POS_SUB_NAME;
                }
                break;
            case R.id.buttonSubName:
                strSubName = editTextSubName.getText().toString();
                if (isNotNull(strSubName, viewId)) {
                    new SubNameCheck().execute(strSubName);
                }
                break;
            case R.id.buttonAddress:
                strAddress = editTextAddress.getText().toString();
                strAddress2 = editTextAddress2.getText().toString();
                //if (isNotNull(strAddress, viewId)) {
                    new Join().execute(strId, strPassword, strEmail, strName, strSubName, strAddress);
               // }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(nowPos == Position.POS_ID)
            finish();
        else{
            viewList[nowPos.getValue()].setVisibility(View.INVISIBLE);
            viewList[nowPos.getValue()-1].setVisibility(View.VISIBLE);
            nowPos = Position.back(nowPos);
        }
    }

    private class IdCheck extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            buttonId.setEnabled(false);
        }

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "type=idCheck&id="+strId;

            BufferedWriter writer = null;
            InputStream inputStream = null;

            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                //아이디 체크 url 적용
                String url = "http://58.237.8.179/Servlet/overlapCheck";
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
            buttonId.setEnabled(true);
            boolean isAble = false;
            if (jsonObject == null)
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            else {

                try {
                    jsonObject = jsonObject.getJSONObject("idCheck");
                    isAble = jsonObject.getBoolean("isAble");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isAble) {
                    layoutId.setVisibility(View.INVISIBLE);
                    layoutPassword.setVisibility(View.VISIBLE);
                    nowPos = Position.POS_PASSWORD;
                } else
                    Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class SubNameCheck extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            buttonSubName.setEnabled(false);
        }

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "type=subNameCheck&subNameCheck="+strSubName;
            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                //아이디 체크 url 적용
                String url = "http://58.237.8.179/Servlet/overlapCheck";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                //커넥션에 각종 정보 설정
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");


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
            buttonSubName.setEnabled(true);
            boolean isAble = false;
            if (jsonObject == null)
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            else {
                try {
                    jsonObject = jsonObject.getJSONObject("subNameCheck");
                    isAble = jsonObject.getBoolean("isAble");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isAble) {
                    layoutSubName.setVisibility(View.INVISIBLE);
                    layoutAddress.setVisibility(View.VISIBLE);
                    nowPos = Position.POS_ADDRESS;
                } else
                    Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임 입니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class Join extends AsyncTask<String, Void, JSONObject> {

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "id="+strId+"&passwd="+strPassword+"&subName="+strSubName
                    +"&name="+strName+"&city="+strAddress+"&streetAddr="+strAddress2+"&email="+strEmail+"&zoneCode="+strZoneCode;
            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                //아이디 체크 url 적용
                String url = "http://58.237.8.179/Servlet/join";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                //커넥션에 각종 정보 설정
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);


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
                    jsonObject = jsonObject.getJSONObject("JoinReport");
                    isSuccessed = jsonObject.getBoolean("isSuccessed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(isSuccessed) {
                    Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

}


