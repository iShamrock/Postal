package iShamrock.Postal.entity;

import java.io.Serializable;

/**
 * Created by lifengshuang on 11/28/14.
 *
 * Added more detailed implementation on 12/29/14.
 * And changed all fields to public, deprecated getters and setters.
 *
 * Modification: add three properties on 2/16/15
 */
public class PostalDataItem implements Serializable {
    /**
     * Data Type, specifically defined image/ short video/ html url.
     * location: [0] for latitude, [1] for longitude.
     */
    public static final int TYPE_IMAGE = 0, TYPE_VIDEO = 1, TYPE_WEB = 2,
            TYPE_AUDIO = 3, TYPE_TEXT = 4;
    public int type;
    public String uri;
    public String text;
    public String time;
    public String title;
    public double[] location;

    public String from_user = "?";
    public String to_user = "?";

    /**
     * Modification: add three properties
     * Constructor not changed, to operate the properties, call corresponding methods
     */
    public String location_text;

    public PostalDataItem(int type, String uri, String text, String time, String title, double[] location, String from_user, String to_user, String location_text) {
        this.type = type;
        this.uri = uri;
        this.text = text;
        this.time = time;
        this.title = title;
        this.location = location;
        this.from_user = from_user;
        this.to_user = to_user;
        this.location_text = location_text;
    }

    public PostalDataItem() {
        type = 4;
        location = new double[2];
    }

    public PostalDataItem to_user(String to_user) {
        this.to_user = to_user;
        return this;
    }

    public PostalDataItem from_user(String from_user) {
        this.from_user = from_user;
        return this;
    }

    public PostalDataItem uri(String uri) {
        this.uri = uri;
        return this;
    }

    public PostalDataItem type(int type) {
        this.type = type;
        return this;
    }

    public PostalDataItem content(String content) {
        this.text = content;
        return this;
    }

    public PostalDataItem time(String time) {
        this.time = time;
        return this;
    }

    public PostalDataItem content(double[] location) {
        this.location = location;
        return this;
    }

    public PostalDataItem latitude(double latitude) {
        this.location[0] = latitude;
        return this;
    }

    public PostalDataItem longitude(double longitude) {
        this.location[1] = longitude;
        return this;
    }

    public PostalDataItem title(String title) {
        this.title = title;
        return this;
    }

    public PostalDataItem locationText(String location_text) {
        this.location_text = location_text;
        return this;
    }

    /*public String getFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");
        return format.format(time.getTime());
    }*/

}
