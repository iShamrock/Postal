package iShamrock.Postal.database;

/**
 * Created by zhangqi on 15/2/27.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
    private static String TAG = "File";

    public static String getSDCardRootPath() {
        // SD卡根目录
        String sDCardRoot = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        return sDCardRoot;
    }

    public static void saveToSDCard(Bitmap bitmap, String filePath,
                                    String fileName) {

        // 将所给文件路径和文件名与SD卡路径连接起来
        String sdcardRoot = getSDCardRootPath();
        // 创建文件路径
        File dir = new File(sdcardRoot + File.separator + filePath);
        Log.i(TAG, "dir: " + dir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File targetFile = new File(dir, fileName);

        try {
            targetFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}