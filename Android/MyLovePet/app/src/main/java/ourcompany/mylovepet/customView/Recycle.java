package ourcompany.mylovepet.customView;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ourcompany.mylovepet.R;

/**
 * Created by REOS on 2017-09-13.
 */

public class Recycle extends RecyclerView.Adapter<ViewHolder>{

    private ArrayList<MenuItem> menuItemsList = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layoutId = 0;
        if(viewType == TYPE_HEADER){
            layoutId = R.layout.menu_header;
        }else{
            layoutId = R.layout.menu_item;
        }

        view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return menuItemsList.get(position).getType();
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String title) {
        MenuItem item = new MenuItem();

        item.setType(TYPE_LIST);
        item.setIcon(icon);
        item.setTitle(title);

        menuItemsList.add(item);
    }

    public void addItem(String header) {
        MenuItem item = new MenuItem();

        item.setType(TYPE_HEADER);
        item.setHeader(header);

        menuItemsList.add(item);
    }
}
