package ourcompany.mylovepet.daummap;

import java.util.List;

/**
 * Created by 쫑티 on 2017-05-15.
 */

public interface OnFinishSearchListener {
    public void onSuccess(List<Item> itemList);
    public void onFail();
}
