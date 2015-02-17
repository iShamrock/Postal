package iShamrock.Postal.database;

import android.database.sqlite.SQLiteDatabase;
import iShamrock.Postal.entity.Friend;
import iShamrock.Postal.entity.PostalDataItem;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/15/15.
 */
public class Database {

    public static SQLiteDatabase database;

    private static final String postal = "POSTAL";
    private static final String postal_id = "POSTAL_ID";
    private static final String postal_from_user = "POSTAL_FROM";
    private static final String postal_to_user = "POSTAL_TO";
    private static final String postal_title = "POSTAL_TITLE";
    private static final String postal_text = "POSTAL_TEXT";
    private static final String postal_pictureULI = "POSTAL_PICTURE_URI";
    private static final String postal_time = "POSTAL_TIME";
    private static final String postal_location = "POSTAL_LOCATION";
    private static final String postal_latitude = "POSTAL_LATITUDE";
    private static final String postal_longitude = "POSTAL_LONGITUDE";
    private static final String postal_videoULI = "POSTAL_VIDEO_URI";
    private static final String postal_recordingULI = "POSTAL_RECORDING_URI";


    private static final String friends = "FRIENDS";
    private static final String friends_id = "FRIENDS_ID";
    private static final String friends_name = "FRIENDS_NAME";
    private static final String friends_phone = "FRIENDS_PHONE";
    private static final String friends_photoURI = "FRIENDS_PHOTO_URI";


    private static final String user = "USER";
    private static final String user_name = "USER_NAME";
//    private static final String user_email = "USER_EMAIL";
    private static final String user_photoURI = "USER_PHOTO_URI";
    private static final String user_phone = "USER_PHONE";
    private static final String user_timeline_cover = "USER_TIMELINE_COVER";


    public static void initDatabase(){
        //postal and journal
        database.execSQL("CREATE TABLE " + postal + " (" + postal_id + " INTEGER primary key autoincrement, "
                + postal_from_user + " TEXT, " + postal_title + " TEXT, " + postal_text + " TEXT, "
                + postal_pictureULI + " TEXT, " + postal_time + " TEXT, " + postal_location + " TEXT, "
                + postal_latitude + " DOUBLE, " + postal_longitude + " TEXT, " + postal_videoULI + " TEXT, "
                + postal_recordingULI + " TEXT, " + postal_to_user + " TEXT, " + ");");

        //friends list
        database.execSQL("CREATE TABLE " + friends + " (" + friends_id + " INTEGER primary key autoincrement, "
                + friends_name + " TEXT, " + friends_photoURI + " TEXT, " + friends_phone + " TEXT, " + ");");

        //user information
        database.execSQL("CREATE TABLE " + user + "(USER_ID INTEGER primary key autoincrement, "
                + user_name + " TEXT, "/* + user_email + " TEXT, "*/ + user_phone + " TEXT, "
                + user_photoURI + " TEXT, " + user_timeline_cover + " TEXT, " + ");");
    }

    /**
     * Called when log in, should be called only once
     * @param username
     */
    public static void loadUserInformation(String username){
        //todo
    }

    /**
     * Called when a postal or a journal is added
     * Notice: The postal is a journal if and only if user_from equals user_to
     * @param postalDataItem
     */
    public static void addPostal(PostalDataItem postalDataItem){
        //todo
    }

    /**
     * Deletion
     * @param postalDataItem
     */
    public static void deletePostal(PostalDataItem postalDataItem){
        //todo
    }

    /**
     *
     * @return all postal
     */
    public static ArrayList<PostalDataItem> getPostal(){
        //todo
        return null;
    }

    /**
     * Modify the user's profile photo
     * @param photoURI
     */
    public static void modifyPhoto(String photoURI){
        //todo
    }

    /**
     *
     * @param username the friend's name
     */
    public static void addFriend(String username){
        //todo
    }

    /**
     *
     * @param username the friend's name
     */
    public static void deleteFriend(String username){
        //todo
    }

    /**
     *
     * @return all friends
     */
    public static ArrayList<Friend> getFriend(){
        //todo
        return null;
    }


}
