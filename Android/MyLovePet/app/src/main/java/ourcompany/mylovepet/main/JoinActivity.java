package ourcompany.mylovepet.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.PostSearchDialog;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

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

        public int getInt(){
            return value;
        }

        public static Position getPosition(int pos){
            if(pos == 0)
                return POS_ID;
            else if(pos == 1)
                return POS_PASSWORD;
            else if(pos == 2)
                return POS_PASSWORD_CHECK;
            else if(pos == 3)
                return POS_EMAIL;
            else if(pos == 4)
                return POS_NAME;
            else if(pos == 5)
                return POS_SUB_NAME;
            else
                return POS_ADDRESS;
        }
    }

    private PostSearchDialog.OnPostSetListener onPostSetListener;

    private Position nowPos;

    private View[] viewList;
    private View layoutId, layoutPassword, layoutPasswordCheck, layoutEmail, layoutName, layoutSubName, layoutAddress;
    private EditText editTextId, editTextPassword, editTextPasswordCheck, editTextEmail, editTextName, editTextSubName, editTextAddress,editTextAddress2;
    private String strId, strPassword, strPasswordCheck, strEmail, strName, strSubName, strAddress,strAddress2,strZoneCode;
    private Button buttonId, buttonSubName;

    private Pattern idFilter;
    private Pattern passwordFilter;

    private ActionBar actionBar;

    private TaskListener idCheckTaskListener;
    private TaskListener subNameCheckTaskListener;
    private TaskListener joinTaskListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
        taskInit();
    }

    //액티비티 초기화
    private void init() {

        idFilter = Pattern.compile("^[a-zA-Z0-9]*$");
        passwordFilter = Pattern.compile("^[!@#$%^&*()a-zA-Z0-9]*$");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar =  getSupportActionBar();
        actionBar.setTitle("아이디");
        actionBar.setDisplayHomeAsUpEnabled(true);

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


        onPostSetListener = new PostSearchDialog.OnPostSetListener() {
            @Override
            public void onPostSet(String zoneCode, String address) {
                strZoneCode = zoneCode;
                editTextAddress.setText(address);
            }
        };

        findViewById(R.id.buttonPostSearch).setOnClickListener(this);


    }

    private void taskInit(){

        //ID체크 리스너 초기화
        idCheckTaskListener = new TaskListener() {
            @Override
            public void preTask() {
                buttonId.setEnabled(false);
            }

            @Override
            public void postTask(Response response) {
                boolean isAble = false;
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject = jsonObject.getJSONObject("idCheck");
                    isAble = jsonObject.getBoolean("isAble");
                    if (isAble){
                        nextPage();
                    }else {
                        Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e ) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "잘못된 데이터", Toast.LENGTH_SHORT).show();
                }finally {
                    buttonId.setEnabled(true);
                }
            }

            @Override
            public void cancelTask() {
                buttonId.setEnabled(true);
            }

            @Override
            public void fairTask() {
                buttonId.setEnabled(true);
            }
        };

        subNameCheckTaskListener = new TaskListener() {
            @Override
            public void preTask() {
                buttonSubName.setEnabled(false);
            }

            @Override
            public void postTask(Response response) {
                boolean isAble = false;

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject = jsonObject.getJSONObject("subNameCheck");
                    isAble = jsonObject.getBoolean("isAble");
                    if (isAble) {
                        nextPage();
                    } else
                        Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }finally {
                    buttonSubName.setEnabled(true);
                }
            }

            @Override
            public void cancelTask() {
                buttonSubName.setEnabled(true);
            }

            @Override
            public void fairTask() {
                buttonSubName.setEnabled(true);
            }
        };

        joinTaskListener = new TaskListener() {
            @Override
            public void preTask() {
            }

            @Override
            public void postTask(Response response) {
                boolean isSuccessed = false;
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject.getJSONObject("JoinReport");
                    isSuccessed = jsonObject.getBoolean("isSuccessed");

                    if(isSuccessed) {
                        Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }finally {
                    finish();
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

    private void idCheckExecute(){
        RequestBody body= new FormBody.Builder()
                .add("type","idCheck")
                .add("id",strId).build();
        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/overlapCheck")
                .post(body)
                .build();
        new RequestTask(request,idCheckTaskListener,getApplicationContext()).execute();
    }

    private void subNameCheckExecute(){
        RequestBody body= new FormBody.Builder()
                .add("type","subNameCheck")
                .add("subNameCheck",strSubName).build();
        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/overlapCheck")
                .post(body)
                .build();
        new RequestTask(request,subNameCheckTaskListener,getApplicationContext()).execute();
    }

    private void joinExecute(){
        RequestBody body= new FormBody.Builder()
                .add("id",strId)
                .add("pass",strPassword)
                .add("subName",strSubName)
                .add("name",strName)
                .add("city",strAddress)
                .add("streetAddr",strAddress2)
                .add("zoneCode",strZoneCode)
                .add("email",strEmail)
                .add("Token",  FirebaseInstanceId.getInstance().getToken())
                .build();
        Request request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/join")
                .post(body)
                .build();
        new RequestTask(request,joinTaskListener,getApplicationContext()).execute();
    }

    //사용자가 값을 입력했는지 확인
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

    //다음 페이지로 화면을 넘기며 현재 위치정보와 타이틀을 바꾼다.
    private void nextPage(){
        viewList[nowPos.getInt()].setVisibility(View.INVISIBLE);
        viewList[nowPos.getInt()+1].setVisibility(View.VISIBLE);
        nowPos = Position.getPosition(nowPos.getInt()+1);
        changeTitle();
    }

    //현재 위치정보를 이용하여 타이틀을 바꾼다
    private void changeTitle(){
        switch (nowPos){
            case POS_ID:
                actionBar.setTitle("아이디");
                break;
            case POS_PASSWORD:
                actionBar.setTitle("패스워드");
                break;
            case POS_PASSWORD_CHECK:
                actionBar.setTitle("패스워드 재입력");
                break;
            case POS_EMAIL:
                actionBar.setTitle("이메일");
                break;
            case POS_NAME:
                actionBar.setTitle("이름");
                break;
            case POS_SUB_NAME:
                actionBar.setTitle("닉네임");
                break;
            case POS_ADDRESS:
                actionBar.setTitle("주소");
                break;
        }
    }

    //버튼이 눌렸을때 해야할 동작을 정의
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
                        idCheckExecute();
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
                        nextPage();
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
                        nextPage();
                    }
                    else Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    //비밀번호를 비교해서 사용자가 원하는 비밀번호를 정확히 입력했는지 확인 끝
                }
                break;
            case R.id.buttonEmail:
                strEmail = editTextEmail.getText().toString();
                //내용이 있으면 다음페이지로
                if (isNotNull(strEmail, viewId)) {
                     nextPage();
                }
                break;
            case R.id.buttonName:
                strName = editTextName.getText().toString();
                //내용이 있으면 다음페이지로
                if (isNotNull(strName, viewId)) {
                    nextPage();
                }
                break;
            case R.id.buttonSubName:
                strSubName = editTextSubName.getText().toString();
                //내용이 있으면 다음페이지로
                if (isNotNull(strSubName, viewId)) {
                    subNameCheckExecute();
                }
                break;
            case R.id.buttonPostSearch:
                new PostSearchDialog(JoinActivity.this,onPostSetListener).show();
            case R.id.buttonAddress:
                strAddress = editTextAddress.getText().toString();
                strAddress2 = editTextAddress2.getText().toString();
                if (isNotNull(strAddress, viewId) && isNotNull(strAddress2,viewId)) {
                    joinExecute();
                }
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

    //뒤로가기 버튼을 눌렀을떄 해야할 동작을 정의
    @Override
    public void onBackPressed() {
        if(nowPos == Position.POS_ID)
            finish();
        else{
            viewList[nowPos.getInt()].setVisibility(View.INVISIBLE);
            viewList[nowPos.getInt()-1].setVisibility(View.VISIBLE);
            nowPos = Position.getPosition(nowPos.getInt()-1);
            changeTitle();
        }
    }

}


