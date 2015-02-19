package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import iShamrock.Postal.R;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/19/15.
 */
public class Test extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        TextView textView = (TextView) findViewById(R.id.test);

        Database.database = openOrCreateDatabase("postal.db", Context.MODE_PRIVATE, null);
        Database.initDatabase();
        Database.addPostal(new PostalDataItem(0, "123", "lalala", "10:10", "this", new double[]{1.0, 2.4}, "lfs", "tzy", "here"));
        Database.addPostal(new PostalDataItem(0, "321", "lalalax", "10:10x", "thisx", new double[]{1.0, 2.4}, "lfsx", "tzyx", "herex"));
        ArrayList<PostalDataItem> dataItems = Database.getPostal();
        String debug;
        if (dataItems.size() != 0){
            debug = "" + dataItems.size();
            System.out.println(debug);
            PostalDataItem last = dataItems.get(dataItems.size() - 1);
            debug = last.from_user + last.title + last.time + last.type + last.location_text + last.text + last.uri;
            System.out.println(debug);
/*            System.out.println(dataItems.get(0).to_user + dataItems.get(0).time + String.valueOf(dataItems.get(0).location[0]));
            textView.setText(dataItems.get(0).to_user + dataItems.get(0).time);*/
        }else {
/*            System.out.println("badddddddddd");
            textView.setText("baDDDDDDDDDD");*/
            debug = "baDDDDDDD";
            System.out.println(debug);
            textView.setText(debug);
        }

        Database.delete();

    }
}
