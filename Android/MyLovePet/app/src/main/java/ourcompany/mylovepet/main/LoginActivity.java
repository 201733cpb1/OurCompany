package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by REOS on 2017-05-08.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextId;
    EditText editTextPassword;
    Button buttonLogin;
    String strId;
    String strPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inIt();
    }

    private void inIt(){
        editTextId = (EditText)findViewById(R.id.editTextId);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        findViewById(R.id.buttonJoin).setOnClickListener(this);
        findViewById(R.id.buttonSearch).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonLogin:
                new Login().execute();
                break;
            case R.id.buttonJoin:
                startActivity(new Intent(this,JoinActivity.class));
                break;
            case R.id.buttonSearch:
                break;
        }
    }


    private class Login extends AsyncTask<String, Void, JSONObject> {

        private HttpURLConnection conn;

        @Override
        protected void onPreExecute() {
            buttonLogin.setEnabled(false);
            strId = editTextId.getText().toString();
            strPassword = editTextPassword.getText().toString();
        }

        @Override
        public JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            String parameter = "id="+strId+"&pass="+strPassword;
            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                String url = "http://58.237.8.179/Servlet/login";
                URL obj = new URL(url);
                conn = (HttpURLConnection) obj.openConnection();

                //커넥션에 각종 정보 설정
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("errorInfo", "error occured!" + e.getMessage());
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            buttonLogin.setEnabled(true);

            if(jsonObject == null)
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            else {
                try {
                    boolean isSuccessed = false;
                    jsonObject = jsonObject.getJSONObject("loginResult");
                    isSuccessed = jsonObject.getBoolean("isSuccessed");
                    if(isSuccessed){
                        User user = User.getIstance();
                        String cookie = conn.getHeaderField("Set-Cookie");
                        user.setCookie(cookie);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
