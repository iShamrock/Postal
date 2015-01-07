package iShamrock.Postal.entity;

import java.io.Serializable;

/**
 * Created by lifengshuang on 11/28/14.
 * Added more detailed implementation on 12/29/14.
 * And changed all fields to public, deprecated getters and setters.
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
    public float[] location;

    public PostalDataItem(int coverType, String coverUrl, int contentType, String content, String time, float[] location) {
        this.coverType = coverType;
        this.coverUrl = coverUrl;
        this.contentType = contentType;
        this.content = content;
        this.time = time;
        this.location = location;
    }

    public PostalDataItem() {
        coverType = 0;
        contentType = 4;
        location = new float[2];
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

    public PostalDataItem content(float[] location) {
        this.location = location;
        return this;
    }

    public PostalDataItem latitude(float latitude) {
        this.location[0] = latitude;
        return this;
    }

    public PostalDataItem longitude(float longitude) {
        this.location[1] = longitude;
        return this;
    }
    /*public String getFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");
        return format.format(time.getTime());
    }*/

}