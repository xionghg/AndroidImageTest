package com.xhg.test.image.pictures;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.source.PicturesLoader;
import com.xhg.test.image.data.source.PicturesRepository;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesPresenter implements PicturesContract.Presenter, LoaderManager.LoaderCallbacks<List<Picture>> {

    private final PicturesRepository mPicturesRepository;

    private final PicturesContract.View mPicturesView;

    private final PicturesLoader mLoader;

    private final LoaderManager mLoaderManager;

    private List<Picture> mCurrentPictures;

    private boolean mFirstLoad;

    public PicturesPresenter(@NonNull PicturesLoader loader, @NonNull LoaderManager loaderManager,
                             @NonNull PicturesRepository PicturesRepository,
                             @NonNull PicturesContract.View PicturesView) {
        mLoader = checkNotNull(loader, "loader cannot be null!");
        mLoaderManager = checkNotNull(loaderManager, "loader manager cannot be null");
        mPicturesRepository = checkNotNull(PicturesRepository, "PicturesRepository cannot be null");
        mPicturesView = checkNotNull(PicturesView, "PicturesView cannot be null!");

        mPicturesView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(0, null, this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public Loader<List<Picture>> onCreateLoader(int id, Bundle args) {
        mPicturesView.setLoadingIndicator(true);
        return mLoader;
    }


    @Override
    public void onLoadFinished(Loader<List<Picture>> loader, List<Picture> data) {
        mPicturesView.setLoadingIndicator(false);

        mCurrentPictures = data;
        if (mCurrentPictures == null) {
            mPicturesView.showLoadingPicturesError();
        } else {
            showFilteredPictures();
        }
    }

    private void showFilteredPictures() {
        List<Picture> PicturesToDisplay = new ArrayList<>();
        if (mCurrentPictures != null) {
            for (Picture Picture : mCurrentPictures) {
                PicturesToDisplay.add(Picture);
            }
        }
        processPictures(PicturesToDisplay);
    }

    @Override
    public void onLoaderReset(Loader<List<Picture>> loader) {
        // no-op
    }

    /**
     * @param forceUpdate Pass in true to refresh the data in the {@link PicturesDataSource}
     */
    public void loadPictures(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad) {
            mFirstLoad = false;
            mPicturesRepository.refreshPictures();
        } else {
            showFilteredPictures();
        }
    }

    private void processPictures(List<Picture> Pictures) {
        if (Pictures.isEmpty()) {
            // Show a message indicating there are no Pictures.
            processEmptyPictures();
        } else {
            // Show the list of Pictures
            mPicturesView.showPictures(Pictures);
        }
    }

    private void processEmptyPictures() {
        mPicturesView.showNoPictures();
    }

    @Override
    public void openPictureDetails(@NonNull Picture requestedPicture) {
        checkNotNull(requestedPicture, "requestedPicture cannot be null!");
        mPicturesView.showPictureDetailsUi(requestedPicture.getId());
    }
}
