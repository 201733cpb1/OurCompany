package ourcompany.mylovepet.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by REOS on 2017-07-07.
 */

public class RequestTask extends AsyncTask<Void,Void, Response> {

    OkHttpClient okHttpClient;
    Request request;
    TaskListener taskListener;
    Context context;

    public RequestTask(Request request, TaskListener taskListener, Context context) {
        this.request = request;
        this.taskListener = taskListener;
        this.context = context;
        okHttpClient = new OkHttpClient();
    }

    @Override
    protected void onPreExecute() {
        taskListener.preTask();
    }

    @Override
    protected Response doInBackground(Void... params) {

        try {
            Response response = okHttpClient.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        taskListener.cancelTask();
    }

    @Override
    protected void onPostExecute(Response response) {
        if(response == null || response.code() != 200) {
            Toast.makeText(context, "서버 통신 실패", Toast.LENGTH_SHORT).show();
            return;
        }else {
            taskListener.postTask(response);
        }
    }


}
