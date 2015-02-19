package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Tong on 02.19.
 */
public class GeoListView extends Activity {
    private ListView listView;

    //private List<String> data = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        setContentView(listView);
    }


}
