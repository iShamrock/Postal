package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.*;
import iShamrock.Postal.LocalData.PostalData;
import iShamrock.Postal.LocalData.PostalDataItem;
import iShamrock.Postal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifengshuang on 11/27/14.
 */
public class Timeline extends Activity {

    private LocationManager locationManager;
    private Location currentLocation;

    private Activity timeline = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        initLocationManager();
        initListView();
        initButtons();
        initLeftDrawer();
    }

    /*@Override
    protected void onResume() {
        System.out.println("on resume");
        super.onResume();
    }*/

    private void initLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                System.out.println("Location: (" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ")");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(timeline, "GPS disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.listView_timeline);
        listView.setAdapter(new SimpleAdapter(this, getListViewData(), R.id.listView_timeline,
                new String[]{"postal", "contents", "time", "location"},
                new int[]{R.id.imageView, R.id.contents_timelineitem, R.id.time_timelineitem, R.id.location_timelineitem}));
    }

    private List<Map<String, Object>> getListViewData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (PostalDataItem item : PostalData.dataItemList) {
            map = new HashMap<String, Object>();
            map.put("postal", R.drawable.test_postal);
            map.put("contents", item.getContents());
            map.put("time", item.getTime());
            map.put("location", item.getLocation());
            list.add(map);
        }
        return list;
    }

    private void initButtons() {
        Button addPostal = (Button) findViewById(R.id.add_timeline);
        addPostal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(timeline, AddPostal_kind.class);
                timeline.startActivity(intent);
            }
        });
    }

    private void initLeftDrawer() {
        String[] titles = new String[]{"Timeline", "In the map", "Make postal", "back to some day"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_timeline);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        } else if (i == 2) {

        } else if (i == 3) {

        }
    }
}
