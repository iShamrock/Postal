package iShamrock.Postal.activity.fore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.activity.Timeline_prev;
import iShamrock.Postal.util.BaiduLocUtil;

/**
 * Created by Tong on 01.07.
 * Splash.
 */
public class Splash extends Activity {
    /*Duration of wait*/
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splash);
        BaiduLocUtil baiduLocUtil = new BaiduLocUtil();
        baiduLocUtil.initialize(getApplicationContext());
        baiduLocUtil.requestLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an intent that will start the main activity*/
                Intent mainIntent = new Intent(Splash.this, Timeline_prev.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        super.onCreate(savedInstanceState);
        ImageView imageView = (ImageView) findViewById(R.id.imageView_splash);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Splash.this.finish();
            }
        });
    }
}
