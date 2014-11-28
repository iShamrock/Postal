package iShamrock.Postal.LocalData;

/**
 * Created by lifengshuang on 11/28/14.
 */
public class PostalDataItem {
    private int postalType;
    private String contents;
    private String time;
    private String location;

    PostalDataItem(int postalType, String contents, String time, String location) {
        this.postalType = postalType;
        this.contents = contents;
        this.time = time;
        this.location = location;
    }

    public int getPostalType() {
        return postalType;
    }

    public String getContents() {
        return contents;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
}
