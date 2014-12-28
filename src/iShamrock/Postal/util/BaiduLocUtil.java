package iShamrock.Postal.util;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Tong on 12.28.
 * BaiduLocUtil encapsulates some features of Baidu locSDK.
 * Used to open the map in current location.
 */
public class BaiduLocUtil {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;

    /**
     * @param context, recommend getApplicationContext() instead of this.
     * @param mBaiduMap, the map to deal with. Dependency injection.
     */
    public void initialize(Context context, BaiduMap mBaiduMap) {
        /* initialize listener*/
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setOpenGps(true);

        myListener = new MyLocationListener(mBaiduMap);
        mLocationClient = new LocationClient(context);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public void requestLocation() {
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.requestLocation();
    }

    class MyLocationListener implements BDLocationListener {
        private BaiduMap mBaiduMap;

        MyLocationListener(BaiduMap mBaiduMap) {
            this.mBaiduMap = mBaiduMap;
        }

        @Override
        public void onReceiveLocation(BDLocation loc) {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(loc.getRadius())
                    .direction(100).latitude(loc.getLatitude())
                    .longitude(loc.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng cenpt = new LatLng(locData.latitude, locData.longitude);
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(15)
                    .build();

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            /* unregister listener after first location succeeded */
            mLocationClient.unRegisterLocationListener(myListener);
        }
    }
}

