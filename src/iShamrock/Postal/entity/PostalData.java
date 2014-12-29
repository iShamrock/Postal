package iShamrock.Postal.entity;

import iShamrock.Postal.R;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lifengshuang on 11/28/14.
 */
public class PostalData {
    public static List<PostalDataItem> dataItemList = new LinkedList<PostalDataItem>();

    public static final int TEST_TYPE = R.id.imageView;

    @Deprecated
    /** see @AddPostal_PostalDataItem, fields and methods changed*/
    public static void addPostal(int postalType, String contents, Calendar time, String location) {
//        dataItemList.add(new PostalDataItem(postalType, contents ,time, location));
    }

}

