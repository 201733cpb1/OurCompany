<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
﻿package ourcompany.mylovepet.main;
=======
package ourcompany.mylovepet.main;
>>>>>>> parent of 936c985... URL 클래스
=======
package ourcompany.mylovepet.main;
>>>>>>> parent of 936c985... URL 클래스
=======
package ourcompany.mylovepet.main;
>>>>>>> parent of 936c985... URL 클래스

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.PetManager;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import ourcompany.mylovepet.main.user.PetManager;
import ourcompany.mylovepet.main.user.PetManager;
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
import ourcompany.mylovepet.main.user.User;

/**
 * Created by REOS on 2017-07-05.
 */

public class MyPageFragment extends Fragment implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {


    public static final int MY_BOARD = 1;
    public static final int MY_SITTER_BOARD = 2;

    MyBoardFragment myBoardFragment;

    PetManager petManager;

    public MyPageFragment(){
        petManager = User.getIstance().getPetManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage,container,false);
        init(view);
        myBoardFragment = new MyBoardFragment();

        User user = User.getIstance();

        TextView textViewNickName = (TextView)view.findViewById(R.id.nickName);
        TextView textViewPetCount = (TextView)view.findViewById(R.id.petCount);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        textViewNickName.setText("닉네임 : "+user.getSunName()+" 님");
        textViewPetCount.setText("나의 펫 수 : " + petManager.getSize()+"");


=======
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스

        textViewNickName.setText("닉네임 : "+user.getSunName()+" 님");
        textViewPetCount.setText("나의 펫 수 : " + petManager.getSize()+"");

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
        return view;
    }


    private void init(View view) {
        view.findViewById(R.id.buttonMyBoard).setOnClickListener(this);
        view.findViewById(R.id.buttonMySitterBoard).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(myBoardFragment.isAdded()){
            return;
        }
        int viewId = v.getId();
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        switch (viewId) {
            case R.id.buttonMyBoard:
                myBoardFragment.setPage(MyBoardFragment.PAGE.BOARD);
                break;
            case R.id.buttonMySitterBoard:
                myBoardFragment.setPage(MyBoardFragment.PAGE.PETSSITER);
                break;
        }
        fragmentTransaction.add(R.id.container,myBoardFragment).addToBackStack(null).commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
