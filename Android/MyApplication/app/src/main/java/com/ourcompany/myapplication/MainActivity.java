package com.ourcompany.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

/**
 * Created by 쫑티 on 2017-05-05.
 */

public class MainActivity extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_market_write);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPopupButtonClick(View button) {
        //PopupMenu 객체 생성.
        PopupMenu popup = new PopupMenu(this, button);

        //설정한 popup XML을 inflate.
        popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
        popup.setGravity(Gravity.CENTER);
        //팝업메뉴 클릭 시 이벤트
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
     /* Search를 선택했을 때 이벤트 실행 코드 작성 */
                        break;

                    case R.id.add:
     /* Add를 선택했을 때 이벤트 실행 코드 작성 */
                        break;

                    case R.id.edit:
     /* Edit를 선택했을 때 이벤트 실행 코드 작성 */
                        break;

                    case R.id.share:
     /* Share를 선택했을 때 이벤트 실행 코드 작성 */
                        break;
                }
                return true;
            }
        });

        popup.show();
    }
}

