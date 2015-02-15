package iShamrock.Postal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import iShamrock.Postal.R;
import iShamrock.Postal.commons.utils.Views;
import iShamrock.Postal.foldablelayout.FoldableListLayout;
import iShamrock.Postal.items.PaintingsAdapter;

/**
 * Created by lifengshuang on 1/8/15.
 */
public class FoldableListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldable_list);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        initLeftDrawer();
        FoldableListLayout foldableListLayout = Views.find(this, R.id.foldable_list);
        foldableListLayout.setAdapter(new PaintingsAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        super.onCreateOptionsMenu(menu);
        MenuItem add = menu.add("");
        add
                .setIcon(R.drawable.plus)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = new Intent();
                        intent.setClass(FoldableListActivity.this, PostalEditor.class);
                        startActivity(intent);
                        return false;
                    }
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private void initLeftDrawer() {
        String[] titles = new String[]{"Postal Box", "In the map", "Edit Postal", "My Posts"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_fold);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_fold);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, titles));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerItemOnClickAction(i);
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

    private void drawerItemOnClickAction(int i) {
        if (i == 1) {
            Intent intent = new Intent();
            intent.setClass(this, PostalNearby.class);
            startActivity(intent);
            finish();
        } else if (i == 2) {
            Intent intent = new Intent();
            intent.setClass(this, PostalEditor.class);
            startActivity(intent);
            finish();
        } else if (i == 0) {
            Intent intent = new Intent();
            intent.setClass(this, Timeline_prev.class);
            startActivity(intent);
            finish();
        }
    }
}
