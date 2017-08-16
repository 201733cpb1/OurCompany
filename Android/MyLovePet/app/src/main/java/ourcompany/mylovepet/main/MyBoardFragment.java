package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ourcompany.mylovepet.R;

/**
 * Created by REOS on 2017-07-06.
 */

public class MyBoardFragment extends Fragment {

    public enum PAGE{
        BOARD,PETSSITER
    };

    private PAGE page;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mypage_board,container,false);
        return view;
    }


    public void setPage(PAGE page) {
        this.page = page;
    }
}
