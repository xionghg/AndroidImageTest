package com.xhg.test.image.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-19.
 */

public class Picture {
    @NonNull
    private String mId;

    @Nullable
    private String mDescription;

    private boolean mCompleted;

    private Bitmap mBitmap;

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
