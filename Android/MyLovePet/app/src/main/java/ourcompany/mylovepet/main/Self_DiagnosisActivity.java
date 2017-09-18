package ourcompany.mylovepet.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.ServerURL;

/**
 * Created by KDM on 2017-07-17.
 */

public class Self_DiagnosisActivity extends AppCompatActivity {
    WebView webview;
    WebSettings mWebSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_diagnosis);

        webview = (WebView) findViewById(R.id.self_diagnosis);
        webview.setWebViewClient(new WebViewClient());
        mWebSettings = webview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        webview.loadUrl(ServerURL.SELF_DIAGNOSIS);
    }
}
