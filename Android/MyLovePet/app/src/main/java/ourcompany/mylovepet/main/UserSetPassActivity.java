package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import ourcompany.mylovepet.R;

/**
 * Created by KDM on 2017-05-16.
 */

public class UserSetPassActivity extends AppCompatActivity implements View.OnClickListener{
    private enum Position {
        POS_PASSWORD(0), POS_PASSWORD_UPDATE(1), POS_PASSWORD_REENTER(2);
        private final int value;
        Position(int value){
            this.value = value;
        }
        public static Position getPosition(int pos){
            if(pos == 0)
                return POS_PASSWORD;
            else if(pos == 1)
                return POS_PASSWORD_UPDATE;
            else
                return POS_PASSWORD_REENTER;
        }
    }

    private Position nowPos;
    private View[] viewList;
    private View layoutPassword,layoutUpdatePassword,layoutReEnterPassword;
    private EditText editTextPassword,editTextUpdatePassword,editTextReEnterPassword;
    private String strPassword,strUpdatePassword,strReEnterPassword;
    private Button profile, pass;
    Pattern passwordFilter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_pass);
        inIt();
    }

    private void inIt(){
        passwordFilter = Pattern.compile("^[!@#$%^&*()a-zA-Z0-9]*$");
        layoutPassword = findViewById(R.id.layout_user_set_pass);
        layoutUpdatePassword= findViewById(R.id.layout_user_set_pass2);
        layoutReEnterPassword = findViewById(R.id.layout_user_set_pass3);

        viewList = new View[3];
        viewList[0] = layoutPassword;
        viewList[1] = layoutUpdatePassword;
        viewList[2] = layoutReEnterPassword;

        layoutPassword.setVisibility(View.VISIBLE);
        layoutUpdatePassword.setVisibility(View.INVISIBLE);
        layoutReEnterPassword.setVisibility(View.INVISIBLE);

        nowPos = Position.POS_PASSWORD;

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUpdatePassword = (EditText) findViewById(R.id.editTextUpdatePassword);
        editTextReEnterPassword = (EditText) findViewById(R.id.editTextReEnterPassword);

        findViewById(R.id.buttonPassword).setOnClickListener(this);
        findViewById(R.id.buttonUpdatePassword).setOnClickListener(this);
        findViewById(R.id.buttonReEnterPassword).setOnClickListener(this);

    }


    private boolean isNotNull(String str, int id) {
        if (str.equals("")) {
            switch (id) {
                case R.id.buttonPassword:
                    Toast.makeText(this, "현재 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonUpdatePassword:
                    Toast.makeText(this, "변경할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.editTextReEnterPassword:
                    Toast.makeText(this, "변경한 비밀번호를 한번 더 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        } else {
            return true;
        }

    }


    private void nextPage(){
        viewList[nowPos.ordinal()].setVisibility(View.INVISIBLE);
        viewList[nowPos.ordinal()+1].setVisibility(View.VISIBLE);
        nowPos = Position.getPosition(nowPos.ordinal()+1);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.buttonPassword:
                //아이디 가져오기
                strPassword = editTextPassword.getText().toString();
                //입력이 되었는지 확인
                if (isNotNull(strPassword, viewId)) {
                    //아이디 형식이 맞는지 확인
                    if (!passwordFilter.matcher(strPassword).matches())
                        Toast.makeText(this, "잘못된 입력", Toast.LENGTH_SHORT).show();
                    else {
                        nextPage();
                        nowPos = Position.POS_PASSWORD_UPDATE;
                    }
                    //아이디 형식이 맞는지 확인 끝
                }
                //입력이 되었는지 확인 끝
                break;
            case R.id.buttonUpdatePassword:
                //패스워드 가져오기
                strUpdatePassword = editTextUpdatePassword.getText().toString();
                //입력이 되었는지 확인
                if (isNotNull(strUpdatePassword, viewId)) {
                    //비밀번호 형식이 맞는지 확인
                    if (!passwordFilter.matcher(strUpdatePassword).matches())
                        Toast.makeText(this, "잘못된 입력", Toast.LENGTH_SHORT).show();
                    else {
                        nextPage();
                        nowPos = Position.POS_PASSWORD_REENTER;
                    }
                    //비밀번호 형식이 맞는지 확인 끝
                }
                //입력이 되었는지 끝
                break;
            case R.id.buttonReEnterPassword:
                strReEnterPassword = editTextReEnterPassword.getText().toString();
                if (isNotNull(strReEnterPassword, viewId)) {
                    //비밀번호를 비교해서 사용자가 원하는 비밀번호를 정확히 입력했는지 확인
                    if (strUpdatePassword.equals(strReEnterPassword)) {
                        layoutReEnterPassword.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getApplicationContext(),UserSettingActivity.class);
                        startActivity(intent);
                    } else Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    //비밀번호를 비교해서 사용자가 원하는 비밀번호를 정확히 입력했는지 확인 끝
                }
                break;
            }
    }


}
