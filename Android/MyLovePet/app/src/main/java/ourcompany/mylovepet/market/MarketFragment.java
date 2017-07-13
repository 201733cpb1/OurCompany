package ourcompany.mylovepet.market;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ourcompany.mylovepet.R;

public class MarketFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_market_main,container,false);

        TabLayout tabs = (TabLayout)view.findViewById(R.id.tabs);
        tabs.setTabTextColors(Color.WHITE, Color.BLACK);
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

        return view;
    }
}
