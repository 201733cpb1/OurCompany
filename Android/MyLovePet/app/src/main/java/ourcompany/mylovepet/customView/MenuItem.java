package ourcompany.mylovepet.customView;

import android.graphics.drawable.Drawable;

/**
 * Created by REOS on 2017-05-02.
 */

public class MenuItem {


    private int type;

    //리스트 변수
    private Drawable icon;
    private String title;

    //헤더 변수
    private String header;


    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setTitle(String itemTitle) {
        this.title = itemTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
