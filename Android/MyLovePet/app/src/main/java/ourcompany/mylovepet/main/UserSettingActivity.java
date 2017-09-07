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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.ServerTaskManager;
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

        listenerInit();
        getPushState();
    }

    private void listenerInit(){

        PushStateTask = new TaskListener() {
            @Override
            public void preTask() {
                lockSwitch();
            }
            @Override
            public void postTask(byte[] bytes) {
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);
                    boolean pushState = false;
                    pushState = jsonObject.getJSONObject("report").getBoolean("result");
                    pushSwitch.setChecked(pushState);
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    unLockSwitch();
                }
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
            public void postTask(byte[] bytes){
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);
                    boolean pushState = jsonObject.getJSONObject("report").getBoolean("result");
                    if(pushState){
                        Toast.makeText(getApplicationContext(),"변경 완료",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"변경 실패",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    unLockSwitch();
                }
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
                .url("http://58.226.2.45/Servlet/userPushState")
                .post(body)
                .build();
        new ServerTaskManager(request, PushStateTask,getApplicationContext()).execute();
    }

    private void setPushState(boolean pushState){
        RequestBody body= new FormBody.Builder()
                .add("isReceive",pushState+"")
                .build();
        Request request = new Request.Builder()
                .url("http://58.226.2.45/Servlet/userPushState")
                .post(body)
                .build();
        new ServerTaskManager(request,setPushTask,getApplicationContext()).execute();
    }

}
