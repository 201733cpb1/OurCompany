package ourcompany.mylovepet.temp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Response;

/**
 * Created by REOS on 2017-08-10.
 */

public class DownloadManager extends Thread{

    Response response;
    DownloadListener downloadListener;
    int size = 1024;
    int length;
    byte[] bytes = new byte[size];

    public DownloadManager(Response response, DownloadListener downloadListener){
        this.response = response;
        this.downloadListener = downloadListener;
    }

    @Override
    public void run() {
        BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            while((length = inputStream.read(bytes, 0, size)) != -1){
                out.write(bytes,0,length);
            }
            bytes = out.toByteArray();
            downloadListener.complete(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            downloadListener.fair();
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
