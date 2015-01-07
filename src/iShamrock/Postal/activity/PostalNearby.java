package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import iShamrock.Postal.R;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.util.BaiduLocUtil;
import iShamrock.Postal.util.BaiduMapUtil;

import java.util.ArrayList;

/**
 * Created by Tong on 12.28.
 */
public class PostalNearby extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocUtil baiduLocUtil;
    private BaiduMapUtil baiduMapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.postal_in_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        baiduMapUtil = new BaiduMapUtil();
        baiduMapUtil.initialize(getApplicationContext(), mBaiduMap, mMapView);

        baiduLocUtil = new BaiduLocUtil();
        baiduLocUtil.initialize(getApplicationContext(), mBaiduMap);
        baiduLocUtil.requestLocation();

        //Intent intent = getIntent();
        //ArrayList<PostalDataItem> data = (ArrayList<PostalDataItem>) intent.getSerializableExtra("data");
        for (PostalDataItem each : PostalData.dataItemList) {
            baiduMapUtil.addMarker(each);
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
