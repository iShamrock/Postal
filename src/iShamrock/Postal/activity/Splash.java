package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import iShamrock.Postal.R;

/**
 * Created by Tong on 01.07.
 */
public class Splash extends Activity {
    /*Duration of wait*/
    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an intent that will start the main activity*/
                Intent mainIntent = new Intent(Splash.this, Postal.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        super.onCreate(savedInstanceState);
    }
}
