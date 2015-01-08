package iShamrock.Postal.util;

import android.text.format.Time;

/**
 * Created by Tong on 01.08.
 *
 */
public class SysInfoUtil {

    /**
     * @return type:"year.month.day.hour.minute.second"
     * Use String.split("."); to get each of time item.
     * */
    public static String getTimeString() {
        Time time = new Time();
        time.setToNow();
        return time.year + "." + time.month + "." + time.monthDay + "." + time.hour + "." + time.minute + "." + time.second;
    }
}
