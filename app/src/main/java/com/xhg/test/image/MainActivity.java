package com.xhg.test.image;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xhg.test.image.strategies.Mandelbrot1;
import com.xhg.test.image.strategies.Mandelbrot2;
import com.xhg.test.image.strategies.Mandelbrot3;

import java.util.Locale;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-04.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmap";

    private ColorHolder mHolder;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView mProgressTextView;
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
                        mStartButton.setEnabled(false);
                        mSaveButton.setEnabled(false);
                        mProgressBar.setProgress(0);
                        mProgressTextView.setText(R.string.no_progress);
                        mImageView.setImageDrawable(getResources().getDrawable(R.drawable.empty_image_300dp));
                    }

                    @Override
                    public void onProgressUpdate(int progress) {
                        updateProgress(progress);
                    }

                    @Override
                    public void onColorsCreated() {
                        Log.d(TAG, "ColorHolder: set color end");
                        mBitmap = mHolder.createBitmap();
                        Log.d(TAG, "ColorHolder: create image end");
                        mImageView.setImageBitmap(mBitmap);
                        mStartButton.setEnabled(true);
                        mSaveButton.setEnabled(true);
                    }
                });
    }

    private void updateProgress(int progress) {
        mProgressBar.setProgress(progress);
        String progressText = String.format(Locale.US, "progress: %3d%%", progress);
        mProgressTextView.setText(progressText);
    }

    private void initViews() {
        mImageView = (ImageView) findViewById(R.id.image);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressTextView = (TextView) findViewById(R.id.progress_text);
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
                String path = FileUtils.writeBitmapToStorage(mBitmap);
                if (!TextUtils.isEmpty(path)) {
                    Toast.makeText(MainActivity.this, "Picture has saved to " + path, Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView recyclerView;
        ListView listView;
    }

}
