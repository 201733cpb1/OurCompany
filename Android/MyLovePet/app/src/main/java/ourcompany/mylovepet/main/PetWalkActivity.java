package ourcompany.mylovepet.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ourcompany.mylovepet.R;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by KDM on 2017-05-16.
 */

public class PetWalkActivity extends AppCompatActivity {
    TextView start,end,distance;
    Button walkstart,walkend,totalend;
    double total_distance;
    LocationManager locationManager;
    LocationListener locationListener;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_walk);

        start = (TextView) findViewById(R.id.starttime);
        end = (TextView) findViewById(R.id.endtime) ;
        distance = (TextView) findViewById(R.id.total_distance);
        walkstart = (Button) findViewById(R.id.walkstart);
        walkend = (Button) findViewById(R.id.walkend);
        totalend = (Button) findViewById(R.id.end);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("펫 산책");
        actionBar.setDisplayHomeAsUpEnabled(true);

        walkstart.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH : mm : ss");
                // nowDate 변수에 값을 저장한다.
                final String formatDate = sdfNow.format(date);
                start.setText(formatDate);
                walkstart.setVisibility(View.GONE);
                walkend.setVisibility(View.VISIBLE);

                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        Location lo1 = new Location("A"); // 위치 저장

                        if(lo1==null){
                            lo1 = location;
                        } else{
                            if(lo1 !=location){
                                total_distance += lo1.distanceTo(location);
                                lo1 = location;
                            }
                            lo1 = location;
                        }
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        //distance.setText("onStatusChanged");
                    }

                    public void onProviderEnabled(String provider) {
                        //distance.setText("onProviderEnabled");
                    }

                    public void onProviderDisabled(String provider) {
                        //distance.setText("onProviderDisabled");
                    }
                };
                // Register the listener with the Location Manager to receive location updates
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000, 10, locationListener);
                locationManager.requestLocationUpdates(GPS_PROVIDER, 1000, 10, locationListener);
            }
        });
        walkend.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH : mm : ss");
                // nowDate 변수에 값을 저장한다.
                final String formatDate = sdfNow.format(date);
                end.setText(formatDate);
                totalend.setVisibility(View.VISIBLE);
                walkend.setVisibility(View.GONE);

                int dist = (int)total_distance;
                distance.setText(dist/10000000+" M");
            }
        });
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

}


