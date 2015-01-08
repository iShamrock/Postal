package iShamrock.Postal.util;

import android.graphics.Bitmap;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            // String _filePath_file.replace(File.separatorChar +
            // file.getName(), "");
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                }
            }
        }
    }
}
