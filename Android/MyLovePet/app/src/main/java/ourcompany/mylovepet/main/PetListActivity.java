package ourcompany.mylovepet.main;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.PetManager;
import ourcompany.mylovepet.main.user.User;


/**
 * Created by KDM on 2017-05-16.
 */

public class PetListActivity extends AppCompatActivity {

    ExpandableListView expandableListView;

    PetManager petManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("동물 관리");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        expandableListView = (ExpandableListView)findViewById(R.id.listView);

        petManager = User.getIstance().getPetManager();

        PetListAdapter adapter = new PetListAdapter(getLayoutInflater());

        expandableListView.setAdapter(adapter);

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = expandableListView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);


                /*  if group item clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    new PetInfoUpdateDialog(PetListActivity.this, petManager.getPet(groupPosition)).show();
                }

                return true;
            }
        });

    }

    //툴바 버튼 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class PetListAdapter extends BaseExpandableListAdapter {

        LayoutInflater inflater;

        public PetListAdapter(LayoutInflater inflater){
            this.inflater = inflater;
        }

        @Override
        public int getGroupCount() {
            return petManager.getSize();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return petManager.getPet(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return petManager.getPet(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = inflater.inflate(R.layout.animal_info_group,null);
            }

            TextView textViewPetName = (TextView)convertView.findViewById(R.id.textViewPetName);
            textViewPetName.setText(petManager.getPet(groupPosition).getName());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = inflater.inflate(R.layout.animal_info,null);
            }

            TextView textViewGender = (TextView)convertView.findViewById(R.id.textViewGender);
            TextView textViewKind = (TextView)convertView.findViewById(R.id.textViewKind);
            TextView textViewBirth = (TextView)convertView.findViewById(R.id.textViewBirth);
            TextView textViewSerialNo = (TextView)convertView.findViewById(R.id.textViewSerialNo);

            textViewGender.setText(petManager.getPet(groupPosition).getGender());
            textViewKind.setText(petManager.getPet(groupPosition).getPetKind()+"");
            textViewBirth.setText(petManager.getPet(groupPosition).getBirth());
            textViewSerialNo.setText(petManager.getPet(groupPosition).getSerialNo()+"");

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


}
