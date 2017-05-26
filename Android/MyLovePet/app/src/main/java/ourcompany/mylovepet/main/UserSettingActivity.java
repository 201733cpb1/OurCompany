package ourcompany.mylovepet.main;

import android.content.Intent;
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
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_update:
                if (chk == false) {
                    profile.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.VISIBLE);
                    chk = true;
                    break;
                } else {
                    profile.setVisibility(View.GONE);
                    pass.setVisibility(View.GONE);
                    chk = false;
                    break;
                }
        }

    }
}
