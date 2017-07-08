package com.xhg.test.image;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xhg.test.image.strategies.Mandelbrot1;

/**
 * Created by xionghg on 17-7-4.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmap";

    private ColorHolder mHolder;
    private ImageView mImageView;
    private Button mStartButton;
    private Button mSaveButton;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initData() {
        Log.d(TAG, "initData");
        mHolder = new ColorHolder();
        // init mHolder and start
        mHolder.setStrategy(new Mandelbrot1())
                .setCallback(new ColorHolder.Callback() {
                    @Override
                    public void onStart() {
                        mSaveButton.setEnabled(false);
                    }

                    @Override
                    public void onColorsCreated() {
                        Log.d(TAG, "ColorHolder: set color end");
                        mBitmap = mHolder.createBitmap();
                        Log.d(TAG, "ColorHolder: create image end");
                        mImageView.setImageBitmap(mBitmap);
                        mSaveButton.setEnabled(true);
                    }
                });
    }

    private void initViews() {
        mImageView = (ImageView) findViewById(R.id.image);
        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHolder.startInParallel();
            }
        });
        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.writeBitmapToStorage(mBitmap);
            }
        });
    }

}
