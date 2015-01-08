package iShamrock.Postal.items;

import android.content.res.Resources;
import android.content.res.TypedArray;
import iShamrock.Postal.R;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;

public class Painting {

    private final int imageId;
    private final String uri;
    private final String title;
    private final String year;
    private final String location;
    private final String content;
    private final boolean local;

    private Painting(int imageId, String uri, String title, String year, String location, String content, boolean local) {
        this.imageId = imageId;
        this.uri = uri;
        this.title = title;
        this.year = year;
        this.location = location;
        this.content = content;
        this.local = local;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public String getUri() {
        return uri;
    }

    public String getContent() {
        return content;
    }

    public boolean isLocal() {
        return local;
    }

    public static Painting[] getAllPaintings(Resources res) {
        String[] titles = res.getStringArray(R.array.paintings_titles);
        String[] years = res.getStringArray(R.array.paintings_years);
        String[] locations = res.getStringArray(R.array.paintings_locations);
        String[] contents = res.getStringArray(R.array.paintings_contents);
        TypedArray images = res.obtainTypedArray(R.array.paintings_images);

        int size = titles.length + PostalData.dataItemList.size();
//        System.err.println(size + " " + PostalData.dataItemList.size() + " " + titles.length);
        Painting[] paintings = new Painting[size];
        int i = 0;

        for (PostalDataItem item : PostalData.dataItemList) {
            paintings[i++] = new Painting(0, item.coverUrl, item.title, item.time, "location", item.content, false);
        }

//        System.err.println(size + " " + PostalData.dataItemList.size() + " " + titles.length);

        for (i = 0; i < titles.length; i++) {
            paintings[i + PostalData.dataItemList.size()] = new Painting(images.getResourceId(i, -1), "", titles[i], years[i], locations[i], contents[i], true);
        }
        return paintings;
    }

}
