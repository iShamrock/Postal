package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.os.Bundle;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import iShamrock.Postal.R;
import iShamrock.Postal.util.BaiduLocUtil;
import iShamrock.Postal.util.BaiduMapUtil;

/**
 * Created by Tong on 02.18.
 */
public class GeoEncodingActivity extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduMapUtil baiduMapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.geo_encoding);
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.postal_in_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        baiduMapUtil = new BaiduMapUtil();
        baiduMapUtil.initialize(getApplicationContext(), mBaiduMap, mMapView);
        /*TODO: Yet the null location has not been dealt.*/
        if (BaiduLocUtil.location != null)
            baiduMapUtil.locateTo(BaiduLocUtil.location);
    }
}
