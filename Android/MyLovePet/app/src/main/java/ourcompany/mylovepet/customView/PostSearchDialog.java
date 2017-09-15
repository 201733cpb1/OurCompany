package ourcompany.mylovepet.customView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by REOS on 2017-04-22.
 */

import ourcompany.mylovepet.R;

public class PostSearchDialog extends Dialog{

    private WebView webView;
    private OnPostSetListener onPostSetListener;
    private Handler handler = new Handler();

    public PostSearchDialog(@NonNull Context context, OnPostSetListener onPostSetListener) {
        super(context);
        this.onPostSetListener = onPostSetListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_post);

        webView = (WebView)findViewById(R.id.web);

        WebSettings wbSettings = webView.getSettings();
        wbSettings.setJavaScriptEnabled(true);
        wbSettings.setDomStorageEnabled(true);
        wbSettings.setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(new JavaScriptMethods(),"android");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        webView.loadUrl("http://58.237.8.179/postSearch.jsp");
=======
        webView.loadUrl("http://58.226.2.45/postSearch.jsp");
>>>>>>> parent of 936c985... URL 클래스
=======
        webView.loadUrl("http://58.226.2.45/postSearch.jsp");
>>>>>>> parent of 936c985... URL 클래스
=======
        webView.loadUrl("http://58.226.2.45/postSearch.jsp");
>>>>>>> parent of 936c985... URL 클래스
=======
        webView.loadUrl("http://58.237.8.179/postSearch.jsp");
>>>>>>> parent of 5c8e350... Merge branch 'AndroidUI' of https://github.com/201733cpb1/OurCompany into AndroidUI
    }


    final private class JavaScriptMethods{
        public JavaScriptMethods(){}

        @android.webkit.JavascriptInterface
        public void setAddress(final String zcode,final String address){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onPostSetListener.onPostSet(zcode,address);
                    dismiss();
                }
            });
        }
    }


    public interface OnPostSetListener{
        public void onPostSet(final String zcode, final String address);
    }

}
