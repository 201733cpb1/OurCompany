package ourcompany.mylovepet.task;

import okhttp3.Response;

/**
 * Created by REOS on 2017-07-07.
 */

public interface TaskListener {

    public void preTask();
    public void postTask(Response response);
    public void cancelTask();
}
