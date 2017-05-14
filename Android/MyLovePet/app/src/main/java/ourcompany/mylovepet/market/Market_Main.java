package ourcompany.mylovepet.market;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ourcompany.mylovepet.R;

public class Market_Main extends AppCompatActivity {

    Toolbar toolbar;

    Fragment clothes;  Fragment feed;
    Fragment health;  Fragment house;
    Fragment snack;  Fragment toy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("사료"));
        tabs.addTab(tabs.newTab().setText("간식"));
        tabs.addTab(tabs.newTab().setText("의류"));
        tabs.addTab(tabs.newTab().setText("하우스"));
        tabs.addTab(tabs.newTab().setText("배변/위생"));
        tabs.addTab(tabs.newTab().setText("식기/물병"));
        tabs.addTab(tabs.newTab().setText("장난감"));
        tabs.addTab(tabs.newTab().setText("목욕/미용"));
        tabs.addTab(tabs.newTab().setText("건강"));

        clothes = new ClothesFragment(); snack = new SnackFragment();
        feed = new FeedFragment(); toy = new ToyFragment();
        health = new HealthFragment();
        house = new HouseFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,feed).commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,feed).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,snack).commit();
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
}
