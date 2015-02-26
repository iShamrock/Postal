package iShamrock.Postal.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import iShamrock.Postal.entity.User;
import iShamrock.Postal.entity.PostalDataItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/15/15.
 */
public class Database {

    public static SQLiteDatabase database;
    public static User me;
    private static Connect connect = new Connect();

    private static boolean test = true;

    private static final String postal = "POSTAL";
    private static final String postal_id = "POSTAL_ID";
    private static final String postal_from_user = "POSTAL_FROM";
    private static final String postal_to_user = "POSTAL_TO";
    private static final String postal_title = "POSTAL_TITLE";
    private static final String postal_text = "POSTAL_TEXT";
    private static final String postal_type = "POSTAL_TYPE";
    private static final String postal_uri = "POSTAL_PICTURE_URI";
    private static final String postal_time = "POSTAL_TIME";
    private static final String postal_location = "POSTAL_LOCATION";
    private static final String postal_latitude = "POSTAL_LATITUDE";
    private static final String postal_longitude = "POSTAL_LONGITUDE";
//    private static final String postal_videoULI = "POSTAL_VIDEO_URI";
//    private static final String postal_recordingULI = "POSTAL_RECORDING_URI";


    private static final String friends = "FRIENDS";
    private static final String friends_id = "FRIENDS_ID";
    private static final String friends_name = "FRIENDS_NAME";
    private static final String friends_phone = "FRIENDS_PHONE";
    private static final String friends_photoURI = "FRIENDS_PHOTO_URI";
    private static final String friends_timeline_cover = "USER_TIMELINE_COVER";


    private static final String user = "USER";
    private static final String user_id = "USER_ID";
    private static final String user_name = "USER_NAME";
//    private static final String user_email = "USER_EMAIL";
    private static final String user_photoURI = "USER_PHOTO_URI";
    private static final String user_phone = "USER_PHONE";
    private static final String user_timeline_cover = "USER_TIMELINE_COVER";


    public static void initDatabase(){
        //postal and journal
        database.execSQL("CREATE TABLE IF NOT EXISTS " + postal + " (" + postal_id + " INTEGER primary key autoincrement, "
                + postal_from_user + " TEXT, " + postal_title + " TEXT, " + postal_text + " TEXT, "
                + postal_uri + " TEXT, " + postal_time + " TEXT, " + postal_location + " TEXT, "
                + postal_latitude + " DOUBLE, " + postal_longitude + " DOUBLE, "/* + postal_videoULI + " TEXT, "*/
                /*+ postal_recordingULI + " TEXT, "*/ + postal_to_user + " TEXT, " + postal_type + " INTEGER" + ");");

        //friends list
        database.execSQL("CREATE TABLE IF NOT EXISTS " + friends + " (" + friends_id + " INTEGER primary key autoincrement, "
                + friends_name + " TEXT, " + friends_photoURI + " TEXT, "
                + friends_phone + " TEXT, " + friends_timeline_cover + " TEXT" + ");");

        //user information
        database.execSQL("CREATE TABLE IF NOT EXISTS " + user + " (" + user_id + " INTEGER primary key autoincrement, "
                + user_name + " TEXT, " + user_phone + " TEXT, "
                + user_photoURI + " TEXT, " + user_timeline_cover + " TEXT" + ");");

        if (test){
//            delete();
            me = new User("lfs", "13666666666", "null", "null");
            addPostal(new PostalDataItem(0, "123", "lalala", "10:10", "this", new double[]{1.0, 2.4}, "lfs", "tzy", "here"));
            addPostal(new PostalDataItem(0, "321", "lalalax", "10:10x", "thisx", new double[]{1.0, 2.4}, "lfsx", "tzyx", "herex"));
        }
    }

    /**
     * Called when log in, should be called only once
     * @param username
     * @return login success or failed
     */
    public static boolean login(String username, String password){
        if (test){
            ContentValues contentValues = new ContentValues();
            contentValues.put(user_name, username);
            contentValues.put(user_phone, "13666666666");
            contentValues.put(user_photoURI, "null");
            contentValues.put(user_timeline_cover, "null");
            database.insert(user, null, contentValues);
            me = new User(username, "13666666666", "null", "null");
            return true;
        }
        else {
            User login = connect.login(username, password);
            if (login == null) {
                return false;
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(user_name, login.getNickname());
                contentValues.put(user_phone, login.getPhone());
                contentValues.put(user_photoURI, login.getPhotoURI());
                contentValues.put(user_timeline_cover, login.getCoverURI());
                database.insert(user, null, contentValues);
                return true;
            }
        }
    }

    /**
     * Called when a postal or a journal is added
     * Notice: The postal is a journal if and only if user_from equals user_to
     * @param postalDataItem
     */
    public static void addPostal(PostalDataItem postalDataItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(postal_from_user, postalDataItem.from_user);
        contentValues.put(postal_title, postalDataItem.title);
        contentValues.put(postal_to_user, postalDataItem.to_user);
        contentValues.put(postal_time, postalDataItem.time);
        contentValues.put(postal_text, postalDataItem.text);
        contentValues.put(postal_type, postalDataItem.type);
        contentValues.put(postal_uri, postalDataItem.uri);
        contentValues.put(postal_location, postalDataItem.location_text);
//        contentValues.put(postal_recordingULI, postalDataItem.recordingUrl);
//        contentValues.put(postal_videoULI, postalDataItem.videoUrl);
        contentValues.put(postal_latitude, postalDataItem.location[0]);
        contentValues.put(postal_longitude, postalDataItem.location[1]);
        database.insert(postal, null, contentValues);
    }

    /**
     * Deletion
     * @param postalDataItem
     */
    public static void deletePostal(PostalDataItem postalDataItem){
        database.delete(postal, postal_time + " = ?", new String[]{postalDataItem.time});
    }

    /**
     *
     * @return all postal
     */
    public static ArrayList<PostalDataItem> getPostal() {
        ArrayList<PostalDataItem> postalDataItemArrayList = new ArrayList<PostalDataItem>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + postal, null);
        while (cursor.moveToNext()){
            PostalDataItem item = new PostalDataItem(
                    cursor.getInt(cursor.getColumnIndex(postal_type)),
                    cursor.getString(cursor.getColumnIndex(postal_uri)),
                    cursor.getString(cursor.getColumnIndex(postal_text)),
                    cursor.getString(cursor.getColumnIndex(postal_time)),
                    cursor.getString(cursor.getColumnIndex(postal_title)),
                    new double[]{cursor.getDouble(cursor.getColumnIndex(postal_latitude)), cursor.getDouble(cursor.getColumnIndex(postal_longitude))},
                    cursor.getString(cursor.getColumnIndex(postal_from_user)),
                    cursor.getString(cursor.getColumnIndex(postal_to_user)),
                    cursor.getString(cursor.getColumnIndex(postal_location))
//                    cursor.getString(cursor.getColumnIndex(postal_videoULI)),
//                   cursor.getString(cursor.getColumnIndex(postal_recordingULI))
            );
            postalDataItemArrayList.add(item);
        }
        for (PostalDataItem item : postalDataItemArrayList) {
            System.out.println(item.type + "|" + item.uri + "|" + item.text + "|" + item.time + "|" + item.title + "|" + item.from_user + "|" + item.to_user + "|" + item.location_text);
        }
        return postalDataItemArrayList;
    }

    /**
     * Modify the user's profile photo
     * @param photoURI
     */
    public static void modifyPhoto(String photoURI) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_photoURI, photoURI);
        database.update(user, contentValues, user_id + " < 10", null);
        //todo:
    }

    /**
     *
     * @param friend
     */
    public static void addFriend(User friend){
        ContentValues contentValues = new ContentValues();
        contentValues.put(friends_name, friend.getNickname());
        contentValues.put(friends_phone, friend.getPhone());
        contentValues.put(friends_photoURI, friend.getPhotoURI());
        contentValues.put(friends_timeline_cover, friend.getCoverURI());
        database.insert(friends, null, contentValues);
        //todo:
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
    public static ArrayList<User> getFriend(){
        //todo
        User a = new User("tzy", "null", "null", "null");
        User b = new User("lfs", "null", "null", "null");
        User c = new User("zq", "null", "null", "null");
        ArrayList<User> x = new ArrayList<User>();
        x.add(a);
        x.add(b);
        x.add(c);
        return x;
    }

    public static void delete(){
        SQLiteDatabase.deleteDatabase(new File(database.getPath()));
    }


}
