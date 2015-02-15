package iShamrock.Postal.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lifengshuang on 2/15/15.
 */
public class Database {

    public static SQLiteDatabase database;
    private static final String postal = "POSTAL";
    private static final String postal_id = "POSTAL_ID";
    private static final String postal_isJournal = "POSTAL_JOURNAL";
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

    public static void initDatabase(){
        database.execSQL("CREATE TABLE " + postal + " (" + postal_id + " INTEGER primary key autoincrement, "
                + postal_isJournal + " INTEGER, " + postal_title + " text, " + postal_text + " text, "
                + postal_pictureULI + " text, " + postal_time + " text, " + postal_location + " text, "
                + postal_latitude + " double, " + postal_longitude + " text, "
                + postal_videoULI + " text, " + postal_recordingULI + " text, " + ");");
        database.execSQL("CREATE TABLE ");

    }
}
