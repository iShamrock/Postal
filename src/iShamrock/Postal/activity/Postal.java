package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import iShamrock.Postal.R;

public class Postal extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent();
        intent.setClass(this, Timeline.class);
        startActivity(intent);
    }
}
