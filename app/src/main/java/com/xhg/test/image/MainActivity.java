package com.xhg.test.image;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmap";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.image);

        Log.d(TAG, "onCreate: start create image");
        ColorHolder holder = new ColorHolder(1024, 1024, new ColorHolder.ColorStrategy() {
            @Override
            public int getRed(int i, int j) {
                double x=0,y=0;
                int k;
                for (k = 0; k++ < 256; ) {
                    double a = x*x - y*y + (i-768.0)/512;
                    y = 2*x*y + (j-512.0)/512;
                    x = a;
                    if (x*x + y*y > 4) break;
                }
                return (int) (Math.log(k)*47);
            }

            @Override
            public int getGreen(int i, int j) {
                double x=0,y=0;
                int k;
                for (k = 0; k++ < 256; ) {
                    double a = x*x - y*y + (i-768.0)/512;
                    y = 2*x*y + (j-512.0)/512;
                    x = a;
                    if (x*x + y*y > 4) break;
                }
                return (int) (Math.log(k)*47);
            }

            @Override
            public int getBlue(int i, int j) {
                double x=0,y=0;
                int k;
                for (k = 0; k++ < 256; ) {
                    double a = x*x - y*y + (i-768.0)/512;
                    y = 2*x*y + (j-512.0)/512;
                    x = a;
                    if (x*x + y*y > 4) break;
                }
                return (int) (128 - Math.log(k)*23);
            }
        });
        Log.d(TAG, "onCreate: set color end");
        Bitmap bitmap = holder.createBitmap();
        Log.d(TAG, "onCreate: create image end");
        imageView.setImageBitmap(bitmap);

        try {
            writeBitmapToStorage(bitmap);
        } catch (IOException e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    private void writeBitmapToStorage(Bitmap bitmap) throws IOException{
        File parent_path = Environment.getExternalStorageDirectory();
        // 可以建立一个子目录专门存放自己专属文件
        File dir = new File(parent_path.getAbsoluteFile(), "testbitmap");
        dir.mkdir();

        File file = new File(dir.getAbsoluteFile(), "1.jpg");
        Log.d(TAG, "文件路径: " + file.getAbsolutePath());
        // 创建这个文件，如果不存在
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        fos.flush();
        fos.close();
        Log.d(TAG, "文件写入成功");
    }

}
