package iShamrock.Postal.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.PostalEditor;
import iShamrock.Postal.entity.PostalDataItem;

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
    public void addMarker(PostalDataItem data) {
        Bitmap origMarkerImage = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.stamp)).getBitmap();
        Bitmap resizedMarkerImage = Bitmap.createScaledBitmap(origMarkerImage, 150, 150, true);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(resizedMarkerImage);

        LatLng marker = new LatLng(data.location[0], data.location[1]);
        OverlayOptions option = new MarkerOptions()
                .position(marker)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new MyOnMarkerClickListener(data));
    }

    class MyOnMarkerClickListener implements BaiduMap.OnMarkerClickListener {
        private PostalDataItem data;

        MyOnMarkerClickListener(PostalDataItem data) {
            this.data = data;
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            Intent intent = new Intent();
            intent.setClass(context, PostalEditor.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
//            intent.putExtra("cover_type", data.coverType);
//            intent.putExtra("cover_url", data.coverUrl);
//            intent.putExtra("content_type", data.contentType);
//            intent.putExtra("content", data.content);
//            intent.putExtra("time", data.time);
//            intent.putExtra("location", data.location);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent);
            return false;
        }
    }
}
