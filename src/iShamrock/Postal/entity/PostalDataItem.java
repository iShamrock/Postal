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
    public static final int TYPE_IMAGE = 0, TYPE_VIDEO = 1, TYPE_WEBVIEW = 2,
            TYPE_HANDWRITING = 3, TYPE_TEXT = 4;
    public int coverType, contentType;
    public String coverUrl;
    public String content;
    public String time;
    public String title;
    public double[] location;

    /**
     * Modification: add three properties
     * Constructor not changed, to operate the properties, call corresponding methods
     */
    public String location_text;
    public String videoUrl;
    public String recordingUrl;

    public PostalDataItem(int coverType, String coverUrl, String title, int contentType, String content, String time, double[] location) {
        this.coverType = coverType;
        this.coverUrl = coverUrl;
        this.title = title;
        this.contentType = contentType;
        this.content = content;
        this.time = time;
        this.location = location;
    }

    public PostalDataItem() {
        coverType = 0;
        contentType = 4;
        location = new double[2];
    }

    public PostalDataItem coverType(int coverType) {
        this.coverType = coverType;
        return this;
    }

    public PostalDataItem coverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        return this;
    }

    public PostalDataItem contentType(int contentType) {
        this.contentType = contentType;
        return this;
    }

    public PostalDataItem content(String content) {
        this.content = content;
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

    public PostalDataItem videoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public PostalDataItem recordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
        return this;
    }
    /*public String getFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");
        return format.format(time.getTime());
    }*/

}
