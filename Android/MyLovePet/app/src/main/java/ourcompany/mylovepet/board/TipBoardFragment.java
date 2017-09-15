package ourcompany.mylovepet.board;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.WebViewFragment;

/**
 * Created by REOS on 2017-09-07.
 */

public class TipBoardFragment extends WebViewFragment{

    View rootView;
    WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_webview,container,false);





        return rootView;
    }



}
