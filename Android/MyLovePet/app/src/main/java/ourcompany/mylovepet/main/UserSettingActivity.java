package ourcompany.mylovepet.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

public class UserSettingActivity extends AppCompatActivity implements OnClickListener {
    Button profile,pass;
    Boolean chk = false;

    TaskListener PushStateTask, setPushTask;

    Switch pushSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        profile = (Button) findViewById(R.id.profile_update);
        pass = (Button) findViewById(R.id.password_update);

        Button btnVisible = (Button) findViewById(R.id.user_info_update);
        btnVisible.setOnClickListener(this);
        findViewById(R.id.profile_update).setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),UserSetProfileActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.password_update).setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),UserSetPassActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonLogOut).setOnClickListener(this);


        pushSwitch = (Switch) findViewById(R.id.pushSwitch);
        pushSwitch.setChecked(true);

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPushState(isChecked);
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("설정");
        actionBar.setDisplayHomeAsUpEnabled(true);

        taskInit();
        getPushState();
    }

    private void taskInit(){

        PushStateTask = new TaskListener() {
            @Override
            public void preTask() {
                lockSwitch();
            }
            @Override
            public void postTask(Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    boolean pushState = jsonObject.getJSONObject("report").getBoolean("result");
                    if(pushState){
                        pushSwitch.setChecked(true);
                    }else {
                        pushSwitch.setChecked(false);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }finally {
                    pushSwitch.setEnabled(true);
                }
            }
            @Override
            public void cancelTask() {
                unLockSwitch();
            }
            @Override
            public void fairTask() {
                unLockSwitch();
            }
        };

        setPushTask = new TaskListener() {
            @Override
            public void preTask() {
                lockSwitch();
            }

            @Override
            public void postTask(Response response){
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    boolean pushState = jsonObject.getJSONObject("report").getBoolean("result");
                    if(pushState){
                        pushSwitch.setChecked(true);
                    }else {
                        pushSwitch.setChecked(false);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }finally {
                    unLockSwitch();
                }
            }

            @Override
            public void cancelTask() {
                unLockSwitch();
            }

            @Override
            public void fairTask() {
                unLockSwitch();
            }

        };

    }


    private void lockSwitch(){
        pushSwitch.setEnabled(false);
    }

    private void unLockSwitch(){
        pushSwitch.setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_update:
                if (chk == false) {
                    profile.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.VISIBLE);
                    chk = true;
                } else {
                    profile.setVisibility(View.GONE);
                    pass.setVisibility(View.GONE);
                    chk = false;
                }
                break;
            case R.id.buttonLogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("account",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("id");
                editor.remove("password");
                editor.commit();
                Intent intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    //툴바에 있는 뒤로가기 버튼이 눌렀을때 해야할 동작을 정의
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPushState(){
        RequestBody body= new FormBody.Builder().build();
        Request request = new Request.Builder()
                .addHeader("Cookie", User.getIstance().getCookie())
                .url("http://58.237.8.179/Servlet/userPushState")
                .post(body)
                .build();
        new RequestTask(request, PushStateTask,getApplicationContext()).execute();
    }

    private void setPushState(boolean pushState){
        RequestBody body= new FormBody.Builder()
                .add("isReceive",pushState+"")
                .build();
        Request request = new Request.Builder()
                .addHeader("Cookie", User.getIstance().getCookie())
                .url("http://58.237.8.179/Servlet/userPushState")
                .post(body)
                .build();
        new RequestTask(request,setPushTask,getApplicationContext()).execute();
    }

}
