package iShamrock.Postal.entity;

import iShamrock.Postal.R;

import java.util.*;

/**
 * Created by lifengshuang on 11/28/14.
 */
public class PostalData {

    public static boolean changedUpdated = true;

    public static List<PostalDataItem> dataItemList = new LinkedList<PostalDataItem>();
    //public static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    public static final int TEST_TYPE = 0;


    @Deprecated
    /** see @AddPostal_PostalDataItem, fields and methods changed*/
    public static void addPostal(int postalType, String contents, Calendar time, String location) {
//        dataItemList.add(new PostalDataItem(postalType, contents ,time, location));
    }

}

