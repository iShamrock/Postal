package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.*;
import iShamrock.Postal.R;
import iShamrock.Postal.util.BaiduLocUtil;
import iShamrock.Postal.util.BaiduMapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tong on 02.18.
 */
public class GeoEncodingActivity extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduMapUtil baiduMapUtil;

    private TextView geo_address;
    private ImageView geo_cancel, geo_ok;
    private ListView geo_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.geo_encoding);


        initCommonComponents();

        initGeoComponents();
    }

    private void initGeoComponents() {
        mBaiduMap = mMapView.getMap();

        baiduMapUtil = new BaiduMapUtil();
        baiduMapUtil.initialize(getApplicationContext(), mBaiduMap, mMapView);
        /*TODO: Yet the null location has not been dealt.*/
        if (BaiduLocUtil.location != null)
            baiduMapUtil.locateTo(BaiduLocUtil.location);

        LatLng latLng = new LatLng(BaiduLocUtil.location.getLatitude(), BaiduLocUtil.location.getLongitude());
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption().location(latLng);

        final GeoCoder geocoder = GeoCoder.newInstance();
        Boolean geoCodingIsSuccessful = geocoder.reverseGeoCode(reverseGeoCodeOption);
        geocoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                // This will never be executed.
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                GeoEncodingActivity.this.geo_address.setText(reverseGeoCodeResult.getAddress());

                List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
                List<String> names = new ArrayList<String>();
                for (PoiInfo poiInfo : poiInfos) names.add(poiInfo.name);
                geo_list.setAdapter(new ArrayAdapter<String>(GeoEncodingActivity.this, R.layout.geo_listview, names));
                geo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String geoEncoding = adapterView.getAdapter().getItem(i).toString();
                        Intent intent = new Intent();
                        intent.putExtra("GeoEncoding", geoEncoding);
                        setResult(JEditor.REQUEST_LOCATION, intent);
                        finish();
                    }
                });
            }
        });
    }


    public void initCommonComponents() {
        geo_address = (TextView) findViewById(R.id.geo_text_address);
        geo_cancel = (ImageView) findViewById(R.id.geo_cancel);
        geo_ok = (ImageView) findViewById(R.id.geo_ok);
        mMapView = (MapView) findViewById(R.id.geo_bmap);
        geo_list = (ListView) findViewById(R.id.geo_list);

        geo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("GeoEncoding", "");
                setResult(JEditor.REQUEST_LOCATION, intent);
                finish();
            }
        });
    }
}
