package ourcompany.mylovepet.market;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ourcompany.mylovepet.R;

public class Market_Main extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_market_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("전체보기"));
        tabs.addTab(tabs.newTab().setText("사료"));
        tabs.addTab(tabs.newTab().setText("간식"));
        tabs.addTab(tabs.newTab().setText("의류"));
        tabs.addTab(tabs.newTab().setText("하우스"));
        tabs.addTab(tabs.newTab().setText("배변/위생"));
        tabs.addTab(tabs.newTab().setText("식기/물병"));
        tabs.addTab(tabs.newTab().setText("장난감"));
        tabs.addTab(tabs.newTab().setText("목욕/미용"));
        tabs.addTab(tabs.newTab().setText("건강"));



        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public void writeStart(View v) {
        Intent intent = new Intent(this,Market_Write.class);
        startActivity(intent);
    }
}
