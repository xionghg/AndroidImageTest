package com.xhg.test.image;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by xionghg on 17-7-4.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmap";

    private ColorHolder holder;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);

        Log.d(TAG, "onCreate: start create image");
        holder = new ColorHolder();
        holder.setStrategy(new Mandelbrot1())
                .setCallback(new ColorHolder.Callback() {
                    @Override
                    public void onColorsCreated() {
                        Log.d(TAG, "onCreate: set color end");
                        Bitmap bitmap = holder.createBitmap();
                        Log.d(TAG, "onCreate: create image end");
                        imageView.setImageBitmap(bitmap);
                        FileUtils.writeBitmapToStorage(bitmap);
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "onError: ");
                    }
                })
                .startInParallel();
    }

}
