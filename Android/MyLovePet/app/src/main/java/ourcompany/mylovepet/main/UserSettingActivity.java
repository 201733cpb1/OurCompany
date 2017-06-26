package ourcompany.mylovepet.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import ourcompany.mylovepet.R;

public class UserSettingActivity extends AppCompatActivity implements OnClickListener {
    Button profile,pass;
    Boolean chk = false;
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


}
