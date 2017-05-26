package ourcompany.mylovepet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;


import java.util.ArrayList;
import java.util.List;

import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by REOS on 2017-04-21.
 */

public class Test extends AppCompatActivity{

    FloatingActionButton fab;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


        viewPager = (ViewPager)findViewById(R.id.pager);

        Pet[] pets;

        pets = new Pet[3];

        pets[0] = new Pet();
        pets[1] = new Pet();
        pets[2] = new Pet();

        User.getIstance().setPets(pets);

        viewPager.setAdapter(new MyAdapter(getLayoutInflater()));

    }
}


class MyAdapter extends PagerAdapter{

    Pet[] pets;
    LayoutInflater inflater;

    public MyAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        pets = User.getIstance().getPets();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.test2,null);

        TextView textViewTemperature = (TextView)view.findViewById(R.id.textViewTemperature);
        TextView textViewWalk = (TextView)view.findViewById(R.id.textViewWalk);
        TextView textViewHeartrate = (TextView)view.findViewById(R.id.textViewHeartrate);

        textViewTemperature.setText(pets[position].getTemperature());
        textViewWalk.setText(pets[position].getWalk());
        textViewHeartrate.setText(pets[position].getHeartrate());

        ((ViewPager)container).addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return pets.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}