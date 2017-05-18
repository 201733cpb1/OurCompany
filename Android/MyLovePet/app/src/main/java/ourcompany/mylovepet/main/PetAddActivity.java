package ourcompany.mylovepet.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import ourcompany.mylovepet.R;

/**
 * Created by REOS on 2017-05-18.
 */

public class PetAddActivity extends AppCompatActivity implements View.OnClickListener{


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
    Button buttonMan, buttonWoman, buttonGender;
    String strPetName,strBirth,strGender,strType,strSerialNo;
    Position nowPos;


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


        findViewById(R.id.buttonPetName).setOnClickListener(this);
        findViewById(R.id.buttonBirth).setOnClickListener(this);
        buttonGender = (Button)findViewById(R.id.buttonGender);
        buttonGender.setEnabled(false);
        buttonGender.setOnClickListener(this);
        findViewById(R.id.buttonType).setOnClickListener(this);
        findViewById(R.id.buttonSerialNo).setOnClickListener(this);

        buttonMan = (Button)findViewById(R.id.buttonMan);
        buttonMan.setOnClickListener(this);
        buttonWoman = (Button)findViewById(R.id.buttonWoman);
        buttonWoman.setOnClickListener(this);

        editTextPetName = (EditText)findViewById(R.id.editTextPetName);
        editTextSerialNo = (EditText)findViewById(R.id.editTextSerialNo);

        datePickerBirth = (DatePicker)findViewById(R.id.datePickerBirth);

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
                buttonSerialNoClick(viewId);
                break;
            case R.id.buttonPetName:
                buttonPetNameClick(viewId);
                break;
            case R.id.buttonBirth:
                buttonBirthClick();
                break;
            case R.id.buttonGender:
                nextPage();
                break;
            case R.id.buttonType:
                //추후 코드 추가
                Toast.makeText(this,strPetName,Toast.LENGTH_SHORT).show();
                Toast.makeText(this,strBirth,Toast.LENGTH_SHORT).show();
                Toast.makeText(this,strGender,Toast.LENGTH_SHORT).show();
                Toast.makeText(this,strSerialNo,Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void buttonSerialNoClick(int viewId){
        strSerialNo = editTextSerialNo.getText().toString();
        if(isNotNull(strSerialNo,viewId)){
            nextPage();
        }else {
            //오류 메세지
        }
    }

    private void buttonPetNameClick(int viewId){
        strPetName = editTextPetName.getText().toString();
        if(isNotNull(strPetName,viewId)){
            nextPage();
        }else {
            //오류 메세지
        }
    }

    private void buttonBirthClick(){
        String year = datePickerBirth.getYear()+"";
        String month = (datePickerBirth.getMonth()+1)+"";
        String day = datePickerBirth.getDayOfMonth()+"";
        strBirth = year+"-"+month+"-"+day;
        nextPage();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void nextPage(){
        layoutList[nowPos.ordinal()].setVisibility(View.INVISIBLE);
        layoutList[nowPos.ordinal()+1].setVisibility(View.VISIBLE);
        nowPos = Position.getPosition(nowPos.ordinal()+1);
    }

    @Override
    public void onBackPressed() {
        if(nowPos == Position.POS_PET_NAME)
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
            }
            return false;
        } else {
            return true;
        }

    }

}
