package ourcompany.mylovepet.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ourcompany.mylovepet.main.user.User;

/**
 * Created by REOS on 2017-05-28.
 */

public class PetInfoAdapter extends FragmentPagerAdapter {


    Fragment[] fragments;

    public PetInfoAdapter(FragmentManager fm) {
        super(fm);
        //user 클래스에 들어있는 펫 정보를 가져온다.
        int length = User.getIstance().getPetManager().getSize();

        //애니멀 갯수만큼 플래그먼트를 생성한다.
        PetInfoFragment[] fragments = new PetInfoFragment[length];

        //플래그먼트 초기화
        for (int i = 0 ; i < length; i++){
            PetInfoFragment fragment = new PetInfoFragment();
            fragment.setPetIndex(i);
            fragments[i] = fragment;
        }
        setFragment(fragments);
    }

    public void setFragment(Fragment[] fragments){
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
