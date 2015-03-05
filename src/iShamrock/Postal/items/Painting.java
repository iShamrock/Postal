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
