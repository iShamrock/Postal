package iShamrock.Postal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import iShamrock.Postal.R;

/**
 * Created by Tong on 12.28.
 * BaiduMApUtil implements some features of baidu map SDK.
 */
public class BaiduMapUtil {
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private Context context;

    /**
     * initialize necessary properties of Baidu Map.
     *
     * @param context,   recommend getApplicationContext() instead of this.
     * @param mBaiduMap, the map to deal with. Dependency injection.
     */
    public void initialize(Context context, BaiduMap mBaiduMap, MapView mMapView) {
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.showZoomControls(false);
        this.mBaiduMap = mBaiduMap;
        this.mMapView = mMapView;
        this.context = context;
    }

    /**
     * Add a Marker(or a Pin) on map
     */
    public void addMarker(BDLocation location) {
        Bitmap origMarkerImage = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.stamp)).getBitmap();
        Bitmap resizedMarkerImage = Bitmap.createScaledBitmap(origMarkerImage, 150, 150, true);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(resizedMarkerImage);

        LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
        OverlayOptions option = new MarkerOptions()
                .position(marker)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
        //  TODO unimplemented listener
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

    }

}
