package ourcompany.mylovepet.market;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ourcompany.mylovepet.R;

public class FeedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState)
    {
        return inflater.inflate(R.layout.flagment_market_feed,container,false);
    }

}
