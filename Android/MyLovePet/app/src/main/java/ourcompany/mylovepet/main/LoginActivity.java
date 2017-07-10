package ourcompany.mylovepet.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.userinfo.User;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

/**
 * Created by REOS on 2017-05-08.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TaskListener{

    private EditText editTextId;
    private EditText editTextPassword;
    private Button buttonLogin;

    private RequestTask requestTask;

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
    protected void onStop() {
        super.onStop();
        requestTask.cancel(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAccount();
    }


    //자동 로그인
    private void loadAccount(){
        SharedPreferences sharedPreferences = getSharedPreferences("account",0);
        String strId = sharedPreferences.getString("id",null);
        String strPassword = sharedPreferences.getString("password",null);

        if(strId == null || strPassword == null){
            return;
        }else { // 자동 로그인 실행
            loginExecute(strId,strPassword);
        }
    }

    private void saveAccount(){
        SharedPreferences sharedPreferences = getSharedPreferences("account",0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id",editTextId.getText().toString());
        editor.putString("password",editTextPassword.getText().toString());
        editor.commit();
    }
    //자동 로그인 end


    private void loginExecute(){
        final String strId = editTextId.getText().toString();
        final String strPassword = editTextPassword.getText().toString();
        loginExecute(strId,strPassword);
    }

    private void loginExecute(final String strId, final String strPassword){

        RequestBody body= new FormBody.Builder().add("id",strId).add("pass",strPassword).build();
        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/login")
                .post(body)
                .build();
        this.requestTask = new RequestTask(request,this,getApplicationContext());
    }


    private void lockView(){
        buttonLogin.setEnabled(false);
        editTextId.setEnabled(false);
        editTextPassword.setEnabled(false);
    }

    private void unLockView(){
        buttonLogin.setEnabled(true);
        editTextId.setEnabled(true);
        editTextPassword.setEnabled(true);
        requestTask = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:
                loginExecute();
                break;
            case R.id.buttonJoin:
                startActivity(new Intent(this,JoinActivity.class));
                break;
            case R.id.buttonSearch:
                break;
        }
    }


    // TaskListener 인터페이스 메소드
    @Override
    public void preTask() {
        lockView();
    }

    @Override
    public void postTask(Response response) {
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            jsonObject = jsonObject.getJSONObject("loginResult");
            boolean isSuccessed = false;
            isSuccessed = jsonObject.getBoolean("isSuccessed");
            if (isSuccessed){
                //아이디 비밀번호 저장
                saveAccount();
                //유저 정보 클래스 불러오기
                User user = User.getIstance();
                //세션 정보를 저장
                String cookie = response.header("Set-Cookie");
                user.setCookie(cookie);
                //메인 화면 실행
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e ) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "잘못된 데이터", Toast.LENGTH_SHORT).show();
        }

        unLockView();
        requestTask = null;
    }

    public void cancelTask(){
        unLockView();
    }
    // TaskListener 인터페이스 메소드 end




    /*private class Login extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            buttonLogin.setEnabled(false);
        }

        @Override
        public Response doInBackground(String... params) {
            RequestBody body= new FormBody.Builder().add("id",strId).add("pass",strPassword).build();
            Request requestTask = new Request.Builder()
                    .url("http://58.237.8.179/Servlet/login")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(requestTask).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            buttonLogin.setEnabled(true);
            if(response == null || response.code() != 200) {
                Toast.makeText(getApplicationContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                jsonObject = jsonObject.getJSONObject("loginResult");
                boolean isSuccessed = false;
                isSuccessed = jsonObject.getBoolean("isSuccessed");
                if (isSuccessed){
                    //아이디 비밀번호 저장
                    saveAccount();
                    //유저 정보 클래스 불러오기
                    User user = User.getIstance();
                    //세션 정보를 저장
                    String cookie = response.header("Set-Cookie");
                    user.setCookie(cookie);
                    //메인 화면 실행
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }


        }

    }*/


}
