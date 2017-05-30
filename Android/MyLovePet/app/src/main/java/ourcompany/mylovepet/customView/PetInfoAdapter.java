package ourcompany.mylovepet.customView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ourcompany.mylovepet.main.AnimalInfoFragment;
import ourcompany.mylovepet.main.MainActivity;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by REOS on 2017-05-28.
 */

public class PetInfoAdapter extends FragmentPagerAdapter {


    public static PetInfoAdapter createAdapter(FragmentManager fm){
        PetInfoAdapter adapter = new PetInfoAdapter(fm);

        //user 클래스에 들어있는 펫 정보를 가져온다.
        Pet[] pets = User.getIstance().getPets();

        //애니멀 갯수만큼 플래그먼트를 생성한다.
        AnimalInfoFragment[] fragments = new AnimalInfoFragment[pets.length];

        //플래그먼트 초기화
        for (int i = 0 ; i < pets.length; i++){
            AnimalInfoFragment fragment = new AnimalInfoFragment();
            fragment.setPet(pets[i]);
            fragments[i] = fragment;
        }

        adapter.setFragment(fragments);

        return adapter;
    }

    Fragment[] fragments;

    private PetInfoAdapter(FragmentManager fm) {
        super(fm);
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
