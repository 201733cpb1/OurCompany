package ourcompany.mylovepet.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("자가진단");
        actionBar.setDisplayHomeAsUpEnabled(true);

        webview = (WebView) findViewById(R.id.self_diagnosis);
        webview.setWebViewClient(new WebViewClient());
        mWebSettings = webview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        webview.loadUrl(ServerURL.SELF_DIAGNOSIS);
    }


    //툴바에 있는 뒤로가기 버튼이 눌렀을때 해야할 동작을 정의
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
