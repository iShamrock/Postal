package iShamrock.Postal.items;

import android.content.res.Resources;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;

import java.util.ArrayList;

public class Painting {


    private PostalDataItem item;

    public Painting(PostalDataItem item) {
        this.item = item;
    }

    public PostalDataItem getItem() {
        return item;
    }

    public static Painting[] getAllPaintings(Resources res) {
/*        String[] titles = res.getStringArray(R.array.paintings_titles);
        String[] years = res.getStringArray(R.array.paintings_years);
        String[] locations = res.getStringArray(R.array.paintings_locations);
        String[] contents = res.getStringArray(R.array.paintings_contents);
        TypedArray images = res.obtainTypedArray(R.array.paintings_images);

        int size = titles.length + PostalData.dataItemList.size();
//        System.err.println(size + " " + PostalData.dataItemList.size() + " " + titles.length);
        Painting[] paintings = new Painting[size];
        int i = 0;

        for (PostalDataItem item : PostalData.dataItemList) {
            paintings[i++] = new Painting(0, item.uri, item.title, item.time, "location", item.text, false);
        }

//        System.err.println(size + " " + PostalData.dataItemList.size() + " " + titles.length);

        for (i = 0; i < titles.length; i++) {
            paintings[i + PostalData.dataItemList.size()] = new Painting(images.getResourceId(i, -1), "", titles[i], years[i], locations[i], contents[i], true);
        }*/

        ArrayList<PostalDataItem> dataItems = Database.getPostal();
        Painting[] paintings = new Painting[dataItems.size() + 1];
        PostalDataItem cover = new PostalDataItem();
        cover.time = "cover";
        paintings[0] = new Painting(cover);
        for (int i = 0; i < dataItems.size(); i++) {
            paintings[i + 1] = new Painting(dataItems.get(dataItems.size() - i - 1));
        }
        return paintings;
    }
}
