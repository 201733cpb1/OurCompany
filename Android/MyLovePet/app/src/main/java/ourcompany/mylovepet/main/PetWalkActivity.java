package ourcompany.mylovepet.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ourcompany.mylovepet.R;

/**
 * Created by KDM on 2017-05-16.
 */

public class PetWalkActivity extends AppCompatActivity {
    TextView start,end;
    Button walkstart,walkend,totalend;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_walk);

        start = (TextView) findViewById(R.id.starttime);
        end = (TextView) findViewById(R.id.endtime) ;
        walkstart = (Button) findViewById(R.id.walkstart);
        walkend = (Button) findViewById(R.id.walkend);
        totalend = (Button) findViewById(R.id.end);

        walkstart.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                final String formatDate = sdfNow.format(date);
                start.setText(formatDate);
                walkstart.setVisibility(View.GONE);
                walkend.setVisibility(View.VISIBLE);
            }
        });
        walkend.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
                // nowDate 변수에 값을 저장한다.
                final String formatDate = sdfNow.format(date);
                end.setText(formatDate);
                totalend.setVisibility(View.VISIBLE);
                walkend.setVisibility(View.GONE);
            }
        });
    }
}
