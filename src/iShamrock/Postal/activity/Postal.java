package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import iShamrock.Postal.R;
import iShamrock.Postal.entity.PostalDataItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Postal extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent();
//        intent.setClass(this, Timeline.class);
//        intent.setClass(this, AddPostal.class);
        List<PostalDataItem> data = new ArrayList<PostalDataItem>();
        data.add(new PostalDataItem(0, "content://media/external/images/media/115219",
                4, "It seems that Christmas time is here once again, and it is time again to bring in the New Year.    This wonderful holiday is the time for us to harvest ,    to relax, to enjoy the happiness with our families, to thank all of our friends ………    We wish the merriest of Christmas to you and  your loved ones, and we wish you happiness and prosperity in the year ahead    It’s my honor to contact with you before, and my duty is to give you our best goods( XXX  产品) and excellent service.    Hope the next year is a prosperous and harvest year for both of us !      Merry Christmas and a happy new year.!    Best wishes for you-my dear friend!", null, new float[]{31.14333f, 121.80528f}));
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) data);
        intent.putExtras(bundle);
        intent.setClass(this, PostalNearby.class);
        startActivity(intent);
    }
}
