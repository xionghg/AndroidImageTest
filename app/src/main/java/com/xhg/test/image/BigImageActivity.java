package com.xhg.test.image;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xhg.test.image.model.StrategyModel;
import com.xhg.test.image.strategies.ColorStrategy;
import com.xhg.test.image.utils.FileUtils;

import java.util.Locale;

public class BigImageActivity extends AppCompatActivity {
    private static final String TAG = "TestBitmap-BigImage";

    private ColorHolder mHolder;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView mProgressTextView;
    private Button mStartButton;
    private Button mSaveButton;
    private Bitmap mBitmap;
    private StrategyModel mStrategyModel;
    private ColorStrategy mStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        initData();
        initViews();
    }

    private void initData() {
        Log.d(TAG, "initData");
        int index = getIntent().getIntExtra("strategy_index", 0);
        mStrategyModel = StrategyModel.getInstance();
        mStrategy = mStrategyModel.getStrategy(index);

        mHolder = new ColorHolder();
        // init mHolder and start
        mHolder.setStrategy(mStrategy)
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
                checkPermissionAndSave();
            }
        });
    }

    private void savePicture() {
        String path = FileUtils.writeBitmapToStorage(mBitmap);
        if (!TextUtils.isEmpty(path)) {
            Toast.makeText(BigImageActivity.this, "Picture has saved to " + path, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissionAndSave() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(BigImageActivity.this, "Need permission to save picture", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        } else {
            savePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePicture();
                } else {
                    Toast.makeText(BigImageActivity.this, "No permission to save picture, you can grant it in Settins",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
