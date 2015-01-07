package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import iShamrock.Postal.R;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;

import java.io.ByteArrayOutputStream;
import java.net.URI;
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
    private AnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        animationView = new AnimationView(this);
        //initLocationManager();

        Intent intent = getIntent();
        //this.dataItems = (ArrayList<PostalDataItem>)intent.getSerializableExtra("data");

        initListView();
        initButtons();
        initLeftDrawer();
    }

    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.listView_timeline);
        listView.setAdapter(new SimpleAdapter(this, getListViewData(), R.layout.timeline_item,
                new String[]{"image"},
                new int[]{R.id.image_list_item}));
//                new String[]{"postal", "content", "time", "location"},
//                new int[]{R.id.imageView, R.id.contents_timelineitem, R.id.time_timelineitem, R.id.location_timelineitem}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(timeline, PostalEditor.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", PostalData.dataItemList.get(i));
                intent.putExtras(bundle);
                Animation animation = AnimationUtils.loadAnimation(timeline, R.anim.scale_image);
                view.startAnimation(animation);
                //foldingAnimation(view);
                timeline.startActivity(intent);
            }
        });
    }

    private void foldingAnimation(View view){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        addContentView(animationView, layoutParams);
        Rect bound = new Rect();
        view.getDrawingRect(bound);
        animationView.folding(timeline, bound);
    }

    private List<Map<String, Object>> getListViewData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

        for (PostalDataItem item : PostalData.dataItemList) {
            map = new HashMap<String, Object>();
            /*map.put("postal", R.drawable.test_postal);
            map.put("content", item.content);
            map.put("time", item.time);
            map.put("location", item.location);*/
            Uri uri = Uri.parse(item.coverUrl);
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("image", R.drawable.default_postal_cover);
            list.add(map);
        }
        return list;
    }

    private void initButtons() {
        ButtonFloat add = (ButtonFloat) findViewById(R.id.btn_add);
        add.setBackgroundColor(0xff1bd411);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(timeline, PostalEditor.class);
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
            Intent intent = new Intent();
            intent.setClass(this, PostalNearby.class);
            startActivity(intent);
        } else if (i == 2) {
            Intent intent = new Intent();
            intent.setClass(this, PostalEditor.class);
            startActivity(intent);
        } else if (i == 3) {

        }
    }

    private static class AnimationView extends View{

        private Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_postal_cover);

        private AnimationView(Context context) {
            super(context);
        }

        public void folding(Context context, Rect location){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_image);
            startAnimation(animation);
        }
    }



/*    private void initLocationManager() {
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
    }*/

}
