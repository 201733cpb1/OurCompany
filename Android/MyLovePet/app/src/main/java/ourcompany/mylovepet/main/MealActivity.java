package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ourcompany.mylovepet.R;

/**
 * Created by KDM on 2017-05-16.
 */

public class MealActivity extends AppCompatActivity {
    LinearLayout mealadd1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        mealadd1 = (LinearLayout) findViewById(R.id.mealadd1);
        findViewById(R.id.meal_update).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),MealCalendarActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.meal_add).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                mealadd1.setVisibility(View.VISIBLE);
            }
        });
    }
}
