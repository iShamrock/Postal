package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.fore.Splash;
import iShamrock.Postal.database.Database;

/**
 * Created by lifengshuang on 2/14/15.
 */
public class Timeline extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        initLeftDrawer();
        initDatabase();
    }

    private void initDatabase() {
        Database.database = openOrCreateDatabase("postal.db", Context.MODE_PRIVATE, null);
        Database.initDatabase();
    }

    private void initLeftDrawer() {
        String[] titles = new String[]{"Timeline", "Friends", "Old version"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_timeline_new);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_timeline_new);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, titles));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    //do nothing
                }
                else if (i == 1){
                    Intent intent = new Intent();
                    intent.setClass(Timeline.this, MyFriends.class);
                    startActivity(intent);
                    finish();
                }
                else if (i == 2){
                    Intent intent = new Intent();
                    intent.setClass(Timeline.this, Splash.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(toggle);
    }
}
