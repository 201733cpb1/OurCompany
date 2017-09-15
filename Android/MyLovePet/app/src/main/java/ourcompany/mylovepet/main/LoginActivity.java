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

import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.ServerURL;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;

/**
 * Created by REOS on 2017-05-08.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextId;
    private EditText editTextPassword;
    private Button buttonLogin;

    private ServerTaskManager loginTask;

    private TaskListener loginTaskListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inIt();
        listenerInit();
    }

    private void inIt(){
        editTextId = (EditText)findViewById(R.id.editTextId);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        findViewById(R.id.buttonJoin).setOnClickListener(this);
        findViewById(R.id.buttonSearch).setOnClickListener(this);
    }

    private void listenerInit(){
        loginTaskListener = new TaskListener() {
            // TaskListener  메소드
            @Override
            public void preTask() {
                lockView();
            }

            @Override
            public void postTask(byte[] bytes) {
                loginTask = null;
                unLockView();
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);
                    jsonObject = jsonObject.getJSONObject("loginResult");
                    boolean isSuccessed = false;
                    isSuccessed = jsonObject.getBoolean("isSuccessed");
                    if (isSuccessed){
                        //유저 정보 클래스 불러오기
                        User user = User.getIstance();
                        //세션 정보를 저장
                        //유저 정보 저장
                        user.setName(jsonObject.getString("name"));
                        user.setSunName(jsonObject.getString("subName"));
                        //아이디 비밀번호 저장
                        saveAccount();
                        //메인 화면 실행
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e ) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "잘못된 데이터", Toast.LENGTH_SHORT).show();
                }
            }

            public void cancelTask(){
                unLockView();
            }

            @Override
            public void fairTask() {
                unLockView();
            }
            // TaskListener  메소드 end
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loginTask != null) {
            loginTask.cancel(true);
        }
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

        editTextId.setText(strId);
        editTextId.setText(strPassword);

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
        editor.putString("password",editTextId.getText().toString());
        editor.commit();
    }
    //자동 로그인 end

    private void loginExecute(){
        final String strId = editTextId.getText().toString();
        final String strPassword = editTextPassword.getText().toString();
        loginExecute(strId,strPassword);
    }

    private void loginExecute(final String strId, final String strPassword){
        SharedPreferences sharedPreferences = getSharedPreferences("token",0);
        String token = sharedPreferences.getString("token",null);

        RequestBody body= new FormBody.Builder()
                .add("id",strId)
                .add("pass",strPassword)
                .add("token", token)
                .build();
        Request request = new Request.Builder()
                .url(ServerURL.LOGIN_URL)
                .post(body)
                .build();
        loginTask = new ServerTaskManager(request, loginTaskListener, getApplicationContext());
        loginTask.execute();
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

}
