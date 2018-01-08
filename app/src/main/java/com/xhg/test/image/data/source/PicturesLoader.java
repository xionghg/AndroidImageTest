package com.xhg.test.image.data.source;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.content.Loader;

import com.xhg.test.image.data.Picture;

import java.util.List;

/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesLoader extends Loader<List<Picture>> {

    private PicturesRepository mRepository;

    public PicturesLoader(Context context, PicturesRepository repository) {
        super(context);
        mRepository = repository;
        LoaderManager loaderManager;
        LoaderManager.LoaderCallbacks loaderCallbacks;
        AsyncTaskLoader asyncTaskLoader;
    }

}
