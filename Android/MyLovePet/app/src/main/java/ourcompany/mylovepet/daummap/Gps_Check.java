package ourcompany.mylovepet.daummap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ourcompany.mylovepet.R;

/**
 * Created by 쫑티 on 2017-05-25.
 */

public class Gps_Check extends AppCompatActivity {

    String gpsEnabled;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_check);

findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        chkGpsService();
    }
});

    }
    private boolean chkGpsService() {

        //GPS가 켜져 있는지 확인함.
        gpsEnabled = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            //gps가 사용가능한 상태가 아니면
            new AlertDialog.Builder(this).setTitle("GPS 설정").setMessage("GPS가 꺼져 있습니다. \nGPS를 활성화 하시겠습니까?").setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    //GPS 설정 화면을 띄움
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();

        }else if((gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            Toast.makeText(getApplicationContext(), "정보를 읽어오는 중입니다.", Toast.LENGTH_LONG).show();
            intent = new Intent(this, Intro.class); //현재 위치 화면 띄우기 위해 인텐트 실행.
            startActivity(intent);
        }
        return false;
    }
    }