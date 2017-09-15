package ourcompany.mylovepet.task;

import okhttp3.Response;

/**
 * Created by REOS on 2017-07-07.
 */

public interface TaskListener {

    void preTask();

    void postTask(byte[] bytes);

    void fairTask();
}
