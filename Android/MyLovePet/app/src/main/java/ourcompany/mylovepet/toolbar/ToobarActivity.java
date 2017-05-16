package ourcompany.mylovepet.toolbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ourcompany.mylovepet.R;

/**
 * Created by REOS on 2017-04-30.
 */

public class ToobarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        dtToggle = new ActionBarDrawerToggle(this,dlDrawer,toolbar,R.string.app_name,R.string.app_name);
        dlDrawer.addDrawerListener(dtToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dtToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        dlDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
