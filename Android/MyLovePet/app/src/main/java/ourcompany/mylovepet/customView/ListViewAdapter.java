package ourcompany.mylovepet.customView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by REOS on 2017-05-02.
 */

import ourcompany.mylovepet.R;

public class ListViewAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;
    private static final int TYPE_MAX = 2;

    private ArrayList<MenuItem> menuItemsList = new ArrayList<>();

    public ListViewAdapter(){}


    @Override
    public int getCount(){
        return menuItemsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int type = getItemViewType(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            switch (type){
                case TYPE_HEADER:
                    convertView = inflater.inflate(R.layout.menu_header,parent,false);
                    TextView header = (TextView)convertView.findViewById(R.id.header);
                    MenuItem headerItem = menuItemsList.get(position);
                    header.setText(headerItem.getHeader());
                    break;
                case TYPE_LIST:
                    convertView = inflater.inflate(R.layout.menu_item, parent, false);
                    // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
                    ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
                    TextView title = (TextView) convertView.findViewById(R.id.title);

                    // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
                    MenuItem listViewItem = menuItemsList.get(position);

                    // 아이템 내 각 위젯에 데이터 반영
                    icon.setImageDrawable(listViewItem.getIcon());
                    title.setText(listViewItem.getTitle());
                    break;
            }


        }



        return convertView;
    }
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return menuItemsList.get(position) ;
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

    @Override
    public int getItemViewType(int position) {
        return menuItemsList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX;
    }
}
