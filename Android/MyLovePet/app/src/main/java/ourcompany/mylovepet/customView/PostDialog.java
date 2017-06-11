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

public class PostDialog extends Dialog{

    WebView webView;
    OnPostSetListener onPostSetListener;
    Handler handler = new Handler();

    public PostDialog(@NonNull Context context) {
        super(context);
    }

    public PostDialog(@NonNull Context context, OnPostSetListener onPostSetListener) {
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
        webView.loadUrl("http://58.237.8.179/postSearch.jsp");
    }

    public void setOnPostSetListener(OnPostSetListener onPostSetListener){
        this.onPostSetListener = onPostSetListener;
    }


    final class JavaScriptMethods{
        public JavaScriptMethods(){}

        @android.webkit.JavascriptInterface
        public void setAddress(final String zcode,final String address){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(onPostSetListener != null) onPostSetListener.onPostSet(zcode,address);
                    dismiss();
                }
            });
        }
    }



    public interface OnPostSetListener{
        public void onPostSet(final String zcode, final String address);
    }

}
