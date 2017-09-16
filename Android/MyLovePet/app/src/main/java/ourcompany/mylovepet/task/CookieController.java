package ourcompany.mylovepet.task;

import android.util.Log;
import android.webkit.CookieManager;

import java.io.IOException;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by REOS on 2017-09-07.
 */

/*
okhttp통신후 쿠키를 쿠키 저장소에 저장 또는 불러오기 위한 클래스
CookieJar 인터페이스를 구현하여 OkHttpClient클래스가 쿠키값을 넘겨 줄수 있도록 구현한다
saveFromResponse(HttpUrl url, List<Cookie> cookies)메소드는
응답한 Url 그리고 response에 있는 쿠키들을 넘겨준다 이 넘겨온 두 인자로
사용자가 적절히 인터페이스를 구현한다.
현재는 CookieManager를 사용하여 쿠키값을 저장한다

loadForRequest(HttpUrl url) 메소드는 인자로 넘어오는 Url주소와 관계있는 쿠키값을
OkHttpClient클래스에서 요청한다.
사용자는 인터페이스를 적절히 구현하여 요청시 사용할 쿠키값을 반환한다.
여기서는 Cookiemanager를 이용하여 쿠키값을 꺼내와 반환한다.
*/
public class CookieController implements CookieJar {
    CookieManager cookieManager;

    public CookieController(){
        cookieManager = CookieManager.getInstance();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        String uri = url.toString();
        Log.d("test",url.toString());
        Log.d("test2",url.uri().toString());

        for(Cookie cookie : cookies){
            cookieManager.setCookie(uri, cookie.toString());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> list = new ArrayList<>();
        //url주소에 해당하는 쿠키매니저로 부터 꺼내오고 ; 구분자로 쿠키를 분리한다.
        String cookie = cookieManager.getCookie(url.toString());

        Log.d("test3",cookie);

        if(cookie != null){
            String[] cookies = cookie.split(";");
            for(String c : cookies){
                list.add(Cookie.parse(url,c));
            }
        }

        return list;
    }


}
