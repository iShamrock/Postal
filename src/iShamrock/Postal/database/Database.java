package iShamrock.Postal.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.entity.User;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/15/15.
 */
public class Database {

    public static SQLiteDatabase database;
    public static User me;
    public static ArrayList<User> allUsers = new ArrayList<User>();

//    private static boolean local = true;
    private static Thread getPostalThread;
    private static Thread getFriendThread;
    private static Thread getAllUserThread;

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

        database.execSQL("DELETE FROM " + postal);
/*        if (local){
//            delete();
            me = new User("lfs", "13666666666", "null", "null");
//            addPostal(new PostalDataItem(0, "123", "lalala", "10:10", "this", new double[]{1.0, 2.4}, "lfs", "tzy", "here"));
//            addPostal(new PostalDataItem(0, "321", "lalalax", "10:10x", "thisx", new double[]{1.0, 2.4}, "lfsx", "tzyx", "herex"));
        }*/
        getPostalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<PostalDataItem> postalDataItems = Connect.getPostalData(me);
                    database.execSQL("delete from " + postal + " where " + postal_id + " > 0");
                    for (PostalDataItem i : postalDataItems) {
                        addPostalToDataBase(i);
                    }
                    System.out.println("getPostal succeed, size is " + postalDataItems.size());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("getPostal Exception");
                }

            }
        });
        getPostalThread.start();

        getAllUserThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    allUsers = Connect.getAllUser();
                    System.out.println("get allUsers succeed, size is " + allUsers.size());
                    for (User i : allUsers){
                        System.out.println("   " + i.getNickname() + " " + i.getPhone());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("allUsers Exception");
                }
            }
        });
        getAllUserThread.start();

        getFriendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<User> myFriends = Connect.getFriendData(me);
                    database.execSQL("delete from " + friends);
                    for (User i : myFriends){
                        addFriendToDatabase(i);
                    }
                    System.out.println("getFriends succeed");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("friends Exception");
                }
            }
        });
        getFriendThread.start();
    }

    public static String getPhoneWithName(String name){
        if (me.getNickname().equals(name)){
            return me.getPhone();
        }
        for (User i : allUsers){
            if (i.getNickname().equals(name)){
                return i.getPhone();
            }
        }
        return "phone not found";
    }

    public static String getNameWithPhone(String phone){
        if (me.getPhone().equals(phone)){
            return me.getNickname();
        }
        for (User i : allUsers){
            System.out.println("phone is \"" + phone + "\" and user phone is \"" + i.getPhone() + "\"");
            if (i.getPhone().equals(phone)){
                return i.getNickname();
            }
        }
        return "name not found";
    }

    public static String getPhotoURIWithName(String name){
        if (me.getNickname().equals(name)){
            return me.getPhotoURI();
        }
        for (User i : allUsers){
            if (i.getNickname().equals(name)){
                return i.getPhotoURI();
            }
        }
        return "";
    }

/*
    public static boolean login(String phone, String password){
        if (local){
            ContentValues contentValues = new ContentValues();
            contentValues.put(user_name, phone);
            contentValues.put(user_phone, "13666666666");
            contentValues.put(user_photoURI, "null");
            contentValues.put(user_timeline_cover, "null");
            database.insert(user, null, contentValues);
            me = new User("test", phone, "null", "null");
            return true;
        }
        else {
            try {
                me = connect.login(phone, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (me == null) {
                return false;
            }
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(user_name, me.getNickname());
                contentValues.put(user_phone, me.getPhone());
                contentValues.put(user_photoURI, me.getPhotoURI());
                contentValues.put(user_timeline_cover, me.getCoverURI());
                database.insert(user, null, contentValues);
                return true;
            }
        }
    }*/
/*
    public static void signUp(User user1, String password){
        final User u = user1;
        final String p = password;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect.signUp(u, p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("sign up");
                }
            }).run();
    }*/

    /**
     * Called when a postal or a journal is added
     * Notice: The postal is a journal if and only if user_from equals user_to
     * @param postalDataItem
     */
    public static void addPostal(PostalDataItem postalDataItem) {
        System.out.println("uri:" + postalDataItem.uri);
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
        final PostalDataItem postalDataItem1 = postalDataItem;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connect.addPostal(postalDataItem1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void addPostalToDataBase(PostalDataItem postalDataItem) {
        System.out.println("add postal to database: " + postalDataItem.text);
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
/*
    public static void deletePostal(PostalDataItem postalDataItem){
        database.delete(postal, postal_time + " = ?", new String[]{postalDataItem.time});
    }*/

    /**
     *
     * @return all postal
     */
    public static ArrayList<PostalDataItem> getPostal() {
        ArrayList<PostalDataItem> postalDataItemArrayList = new ArrayList<PostalDataItem>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + postal, null);
        System.out.println("get Postal from database");
        while (cursor.moveToNext()) {
            System.out.println(    "text is: " + cursor.getString(cursor.getColumnIndex(postal_text)));
            String str = cursor.getString(cursor.getColumnIndex(postal_uri));
            int type = cursor.getInt(cursor.getColumnIndex(postal_type));
            if (str.contains("download_test") && type == 0) {
                str = str.substring(0, str.indexOf("emulated")) + "emulated/0/" + str.substring(str.indexOf("cache"), str.length());
            }
            PostalDataItem item = new PostalDataItem(
                    type,
                    str,
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
//        getPostalThread.start();
        return postalDataItemArrayList;
    }


/*
    public static void modifyPhoto(String photoURI) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(user_photoURI, photoURI);
        database.update(user, contentValues, user_id + " < 10", null);
        //todo:
    }
*/

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
        final User f = friend;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connect.addFriend(me, f);
                    System.out.println("addFriend succeed");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("addFriend Exception");
                }
            }
        }).start();
    }

    private static void addFriendToDatabase(User friend){
        ContentValues contentValues = new ContentValues();
        contentValues.put(friends_name, friend.getNickname());
        contentValues.put(friends_phone, friend.getPhone());
        contentValues.put(friends_photoURI, friend.getPhotoURI());
        contentValues.put(friends_timeline_cover, friend.getCoverURI());
        database.insert(friends, null, contentValues);
    }

    /**
     *
     * @param username the friend's name
     */
    public static void deleteFriend(String username){
        //todo
    }

    public static ArrayList<User> getAllUsers(){
//        getAllUserThread.start();
        return allUsers;
    }

    /**
     *
     * @return all friends
     */
    public static ArrayList<User> getFriend() {
        ArrayList<User> myFriends = new ArrayList<User>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + friends, null);
        while (cursor.moveToNext()){
            User temp = new User(
                    cursor.getString(cursor.getColumnIndex(friends_name)),
                    cursor.getString(cursor.getColumnIndex(friends_phone)),
                    cursor.getString(cursor.getColumnIndex(friends_photoURI)),
                    cursor.getString(cursor.getColumnIndex(friends_timeline_cover)));
            myFriends.add(temp);
        }
//        getFriendThread.start();
        return myFriends;
    }

//    public static void delete(){
//        SQLiteDatabase.deleteDatabase(new File(database.getPath()));
//    }


}
