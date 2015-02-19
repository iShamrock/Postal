package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.publishers.PEditor;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.util.BaiduLocUtil;
import iShamrock.Postal.util.BaiduMapUtil;

/**
 * Created by Tong on 12.28.
 */
public class PostalNearby extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduMapUtil baiduMapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.postal_in_map);
        initLeftDrawer();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        baiduMapUtil = new BaiduMapUtil();
        baiduMapUtil.initialize(getApplicationContext(), mBaiduMap, mMapView);
        /*TODO: Yet the null location has not been dealt.*/
        if (BaiduLocUtil.location != null)
            baiduMapUtil.locateTo(BaiduLocUtil.location);

        //Intent intent = getIntent();
        //ArrayList<PostalDataItem> data = (ArrayList<PostalDataItem>) intent.getSerializableExtra("data");
        for (PostalDataItem each : PostalData.dataItemList) {
            baiduMapUtil.addMarker(each);
        }
    }
    private void initLeftDrawer() {
        String[] titles = new String[]{"Postal Box", "In the map", "Edit Postal", "My Posts"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_postalnearby);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_postalnearby);
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
        if (i == 0) {
            Intent intent = new Intent();
            intent.setClass(this, Timeline_prev.class);
            startActivity(intent);
            finish();
        } else if (i == 2) {
            Intent intent = new Intent();
            intent.setClass(this, PEditor.class);
            startActivity(intent);
            finish();
        } else if (i == 3) {
            Intent intent = new Intent();
            intent.setClass(this, FoldableListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


}
