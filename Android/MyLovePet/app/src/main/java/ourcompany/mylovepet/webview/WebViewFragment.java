package ourcompany.mylovepet.webview;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.MainActivity;
import ourcompany.mylovepet.main.OnBackKeyPressListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by REOS on 2017-09-07.
 */

public class WebViewFragment extends Fragment implements OnBackKeyPressListener {
    View rootView;
    WebView webView;
    ValueCallback fileCallBack;

    public static final int REQUEST_CODE_TAKE_PHOTO = 100;

    public static WebViewFragment createWebViewFragment(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        webViewFragment.setArguments(bundle);

        return webViewFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK){
            if(fileCallBack != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fileCallBack.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data));
                }else {
                    fileCallBack.onReceiveValue(data.getData());
                }
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_webview, container, false);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                fileCallBack = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
                return true;
            }
        });
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        webView.loadUrl(url);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setOnBackKeyPressListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).setOnBackKeyPressListener(null);
    }


    public View findViewById(int id) {
        if (rootView != null) {
            return rootView.findViewById(id);
        } else {
            return null;
        }

    }

    @Override
    public boolean onBack() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }

}
