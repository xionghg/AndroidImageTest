package com.xhg.test.image;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xionghg on 17-7-4.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void writeBitmapToStorage(Bitmap bitmap) {
        File parent_path = Environment.getExternalStorageDirectory();
        // 可以建立一个子目录专门存放自己专属文件
        File dir = new File(parent_path.getAbsoluteFile(), "testbitmap");
        dir.mkdir();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);
        String timeStr = sdf.format(new Date());
        File file = new File(dir.getAbsoluteFile(), timeStr + ".jpg");
        Log.d(TAG, "文件路径: " + file.getAbsolutePath());

        try {
            // 创建这个文件，如果不存在
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.d(TAG, "文件写入成功");
        } catch (IOException e) {
            Log.e(TAG, "writeBitmapToStorage: ", e);
        }

    }
}
