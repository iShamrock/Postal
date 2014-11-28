package iShamrock.Postal.LocalData;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by lifengshuang on 11/28/14.
 */
public class PostalDataItem {
    private int postalType;
    private String contents;
    private Calendar time;
    private String location;

    PostalDataItem(int postalType, String contents, Calendar time, String location) {
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
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");
        return format.format(time.getTime());
    }

    public Calendar getCalender(){
        return time;
    }

    public String getLocation() {
        return location;
    }
}
