package iShamrock.Postal.LocalData;

import iShamrock.Postal.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lifengshuang on 11/28/14.
 */
public class PostalData {
    public static List<PostalDataItem> dataItemList = new LinkedList<PostalDataItem>();

    public static final int TEST_TYPE = R.id.imageView;

    public static void addPostal(int postalType, String contents, String time, String location){
        dataItemList.add(new PostalDataItem(postalType, contents ,time, location));
    }

}

