package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import iShamrock.Postal.R;
import iShamrock.Postal.database.Database;

/**
 * Created by lifengshuang on 2/19/15.
 */
public class Test extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        Database.database = openOrCreateDatabase("postal.db", Context.MODE_PRIVATE, null);
        Database.initDatabase();

    }
}
