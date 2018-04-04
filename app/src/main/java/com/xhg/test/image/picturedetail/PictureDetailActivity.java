package com.xhg.test.image.picturedetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xhg.test.image.R;
import com.xhg.test.image.data.StrategyFactory;
import com.xhg.test.image.strategies.BitmapGenerator;
import com.xhg.test.image.strategies.ColorStrategy;
import com.xhg.test.image.utils.FileUtils;
import com.xhg.test.image.utils.Log;

import java.util.Locale;

public class PictureDetailActivity extends AppCompatActivity {
    private static final String TAG = "PictureDetailActivity";
    public static final String EXTRA_PICTURE_ID = "extra_picture_id";
    private BitmapGenerator mGenerator;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView mHeadText;
    private TextView mProgressTextView;
    private Button mStartButton;
    private Button mSaveButton;
    private Button mCutButton;
    private Bitmap mBitmap;
    private StrategyFactory mStrategyModel;
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
        int index = getIntent().getIntExtra(EXTRA_PICTURE_ID, 0);
        mStrategyModel = StrategyFactory.getInstance();
        mStrategy = mStrategyModel.getStrategy(index);
        BitmapGenerator.Callback callback = new BitmapGenerator.Callback() {
            @Override
            public void onStart() {
                mStartButton.setText(R.string.button_text_stop);
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
            public void onBitmapCreated(Bitmap bitmap) {
                Log.d(TAG, "onBitmapCreated");
                mBitmap = bitmap;
                mImageView.setImageBitmap(bitmap);
                mStartButton.setText(R.string.button_text_start);
                mSaveButton.setEnabled(true);
            }

            @Override
            public void onCanceled() {
                Log.d(TAG, "onCanceled: ");
                mStartButton.setText(R.string.button_text_start);
            }
        };
        mGenerator = BitmapGenerator.with(callback).setStrategy(mStrategy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
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
        mSaveButton = (Button) findViewById(R.id.save_button);
        mCutButton = (Button) findViewById(R.id.cut_button);
        mHeadText = findViewById(R.id.head_text);

        mStartButton.setOnClickListener(v -> onStartButtonClick());
        mSaveButton.setOnClickListener(v -> checkPermissionAndSave());
        mHeadText.setText(mStrategy.getDescription());
    }

    private void onStartButtonClick() {
        if (mStartButton.getText().equals(getString(R.string.button_text_start))) {
            mGenerator.startInParallel();
        } else {
            mGenerator.cancel();
        }
    }

    private void savePicture() {
        String path = FileUtils.writeBitmapToStorage(mBitmap);
        if (!TextUtils.isEmpty(path)) {
            Toast.makeText(PictureDetailActivity.this, "Picture has saved to " + path, Toast.LENGTH_SHORT).show();

            final String picPath = path;
            mCutButton.setEnabled(true);
            mCutButton.setOnClickListener(v -> {
                Intent intent = new Intent(PictureDetailActivity.this, PictureCutActivity.class);
                intent.putExtra("pic_path", picPath);
                startActivity(intent);
            });
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
                Toast.makeText(PictureDetailActivity.this, "Need permission to save picture", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PictureDetailActivity.this, "No permission to save picture, you can grant it in Settins",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
