package iShamrock.Postal.util;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Tong on 12.28.
 * BaiduLocUtil encapsulates some features of Baidu locSDK.
 * Used to open the map in current location.
 */
public class BaiduLocUtil {
    public LocationClient mLocationClient;
    public BDLocationListener myListener;
    public static BDLocation location;

    /**
     * @param context,   recommend getApplicationContext() instead of this.
     */
    public void initialize(Context context) {
        /* initialize listener*/
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setOpenGps(true);

        myListener = new MyLocationListener();
        mLocationClient = new LocationClient(context);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public void requestLocation() {
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.requestLocation();
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation loc) {
            location = loc;

            /* unregister listener after first location succeeded */
            mLocationClient.unRegisterLocationListener(myListener);
        }
    }
}

