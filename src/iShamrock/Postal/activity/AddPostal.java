package iShamrock.Postal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import iShamrock.Postal.R;

/**
 * Created by Tong on 12.24.
 */
public class AddPostal extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostal);

        WebView postalContainer = (WebView) findViewById(R.id.postal_container);

        /* Add javascript support.*/
        WebSettings webSettings = postalContainer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLightTouchEnabled(true);
        webSettings.setSupportZoom(false);

        postalContainer.setHapticFeedbackEnabled(true);
        // Avoid "ERR_CACHE_MISS" explicitly
//        if (Build.VERSION.SDK_INT >= 19)
        postalContainer.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        BufferedInputStream in = null;
//        try {
//            in = new BufferedInputStream(getAssets().open("web/content.html"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        postalContainer.loadUrl("file:///android_asset/web/content.html");

    }
}
