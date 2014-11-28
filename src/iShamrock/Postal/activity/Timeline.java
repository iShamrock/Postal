package iShamrock.Postal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import iShamrock.Postal.R;

/**
 * Created by lifengshuang on 11/27/14.
 */
public class Timeline extends Activity{

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
    }
}
