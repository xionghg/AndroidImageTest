package com.xhg.test.image.picturedetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xhg.test.image.R;
import com.xhg.test.image.imageloader.ImageResizer;

public class PictureCutActivity extends AppCompatActivity {

    ImageView mOriginPic;
    ImageView mSmallPic;
    ImageView mBigPic;

    EditText mTopValue;
    EditText mLeftValue;
    EditText mBottomValue;
    EditText mRightValue;

    Button mGenSmall;
    Button mGenBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_cut);
        String path = getIntent().getStringExtra("pic_path");
        initViews();
        Bitmap bitmap = new ImageResizer().decodeSampledBitmapFromPath(path, 200, 200);
        mOriginPic.setImageBitmap(bitmap);
    }


    private void initViews() {
        mOriginPic = findViewById(R.id.origin_pic);
        mSmallPic = findViewById(R.id.pic_small);
        mBigPic = findViewById(R.id.pic_big);

        mTopValue = findViewById(R.id.top_value);
        mLeftValue = findViewById(R.id.left_value);
        mBottomValue = findViewById(R.id.bottom_value);
        mRightValue = findViewById(R.id.right_value);

        mGenSmall = findViewById(R.id.gen_small_pic);
        mGenBig = findViewById(R.id.gen_big_pic);
    }
}
