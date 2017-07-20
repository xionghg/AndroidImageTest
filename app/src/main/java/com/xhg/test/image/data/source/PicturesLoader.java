package com.xhg.test.image.data.source;

import android.content.Context;
import android.support.v4.content.Loader;

import com.xhg.test.image.data.Picture;

import java.util.List;

/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesLoader extends Loader<List<Picture>> {
    public PicturesLoader(Context context, PicturesRepository repository) {
        super(context);
    }

}
