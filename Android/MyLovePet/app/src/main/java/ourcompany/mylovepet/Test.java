package ourcompany.mylovepet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import ourcompany.mylovepet.main.AnimalInfoFragment;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by REOS on 2017-04-21.
 */

public class Test extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager2);

        Pet[] pets = new Pet[2];
        pets[0] = new Pet();
        pets[0].setName("아아아");
        pets[1] = new Pet();
        pets[1].setName("ddddddd");

        viewPager.setAdapter(new Adapter(getLayoutInflater(),pets));

    }
}


class Adapter extends PagerAdapter{

    Pet[] pets;
    LayoutInflater inflater;

    public Adapter(LayoutInflater inflater, Pet[] pets){
        this.pets = pets;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return pets.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.pet,container,false);

        TextView textViewPetName = (TextView)view.findViewById(R.id.textViewPetName);
        textViewPetName.setText(pets[position].getName());

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}