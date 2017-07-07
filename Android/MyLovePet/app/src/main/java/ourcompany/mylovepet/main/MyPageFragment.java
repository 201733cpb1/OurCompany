package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.customView.ListViewAdapter;
import ourcompany.mylovepet.market.Market_Intro;
import ourcompany.mylovepet.petsitter.PetSitterAddFragment;
import ourcompany.mylovepet.petsitter.PetSitterFindActivity;

/**
 * Created by REOS on 2017-07-05.
 */

public class MyPageFragment extends Fragment implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;


    public static final int MY_BOARD = 1;
    public static final int MY_SITTER_BOARD = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mypage,container,false);

        init(view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        view.findViewById(R.id.buttonMyBoard).setOnClickListener(this);
        view.findViewById(R.id.buttonMySitterBoard).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent = new Intent(getContext(),MyBoardActivity.class);
        switch (viewId) {
            case R.id.buttonMyBoard:
                intent.putExtra("board", MY_BOARD);
                startActivity(intent);
                break;
            case R.id.buttonMySitterBoard:
                intent.putExtra("board", MY_SITTER_BOARD);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
