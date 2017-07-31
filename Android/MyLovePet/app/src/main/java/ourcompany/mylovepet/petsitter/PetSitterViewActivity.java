package ourcompany.mylovepet.petsitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ourcompany.mylovepet.R;

/**
 * Created by KDM on 2017-05-16.
 */

public class PetSitterViewActivity extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_petsitter_view,container,false);
        return view;
    }

}
