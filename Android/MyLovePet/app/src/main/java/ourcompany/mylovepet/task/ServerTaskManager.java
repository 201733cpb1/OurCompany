package ourcompany.mylovepet.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.daum.android.map.util.PersistentKeyValueStore;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ourcompany.mylovepet.main.user.User;

/**
 * Created by REOS on 2017-07-07.
 */

public class ServerTaskManager extends AsyncTask<Void,Void, byte[]> {

    OkHttpClient okHttpClient;
    Request request;
    TaskListener taskListener;
    Context context;

    public ServerTaskManager(Request request, TaskListener taskListener, Context context) {
        this.request = request;
        this.taskListener = taskListener;
        this.context = context;


        okHttpClient = new OkHttpClient()
                .newBuilder()
                .cookieJar(new CookieController())
                .build();
    }

    @Override
    protected void onPreExecute() {
        taskListener.preTask();
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        Response response = null;
        byte[] bytes = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if(response == null) {
                Log.d("serverTask","do not connection");
                cancel(true);
            }else if(response.code() != 200){
                Log.d("serverTask","error CODE : " + response.code());
                Log.d("body",response.body().string());
                cancel(true);
            }else {
                bytes = response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response != null){
                response.close();
            }
        }
        return bytes;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        if(bytes != null){
            taskListener.postTask(bytes);
        }else {
            taskListener.fairTask();
        }
    }

    @Override
    protected void onCancelled() {
        taskListener.fairTask();
    }


}
