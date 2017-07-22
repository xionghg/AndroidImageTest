package com.xhg.test.image.pictures;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.StrategyFactory;
import com.xhg.test.image.data.source.PicturesLoader;
import com.xhg.test.image.data.source.PicturesRepository;
import com.xhg.test.image.strategies.ColorGenerator;
import com.xhg.test.image.strategies.GeneratorManger;

import java.util.ArrayList;
import java.util.List;

import static com.xhg.test.image.utils.CheckUtils.checkNotNull;


/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesPresenter implements PicturesContract.Presenter, GeneratorManger.GeneratorCallback {
    private static final String TAG = "PicturesPresenter";

    private final PicturesRepository mPicturesRepository;

    private final PicturesContract.View mPicturesView;

    private final GeneratorManger mGeneratorManger;

    private List<Picture> mCurrentPictures;

    private boolean mFirstLoad;

    public PicturesPresenter(@NonNull GeneratorManger generatorManger,
                             @NonNull PicturesRepository PicturesRepository,
                             @NonNull PicturesContract.View PicturesView) {
        mGeneratorManger = checkNotNull(generatorManger, "loader manager cannot be null");
        mPicturesRepository = checkNotNull(PicturesRepository, "PicturesRepository cannot be null");
        mPicturesView = checkNotNull(PicturesView, "PicturesView cannot be null!");

        mPicturesView.setPresenter(this);
    }

    @Override
    public void start() {
        mGeneratorManger.initGenerator(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void onGenerateStart(int size) {
        mPicturesView.showEmptyPictures(size);
    }

    @Override
    public ColorGenerator onCreateGenerator(int index, Bundle args) {
        Log.e(TAG, "onCreateLoader: ");
        mPicturesView.setLoadingIndicator(true);
        ColorGenerator colorGenerator = new ColorGenerator(512, 512);
        colorGenerator.setStrategy(StrategyFactory.getInstance().getStrategy(index))
                .setCallback(new ColorGenerator.SimpleCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgressUpdate(int progress) {

                    }

                    @Override
                    public void onColorsCreated() {

                    }

                });
        return colorGenerator;
    }


    @Override
    public void onGenerateFinished(ColorGenerator generator, Picture data) {
        Log.e(TAG, "onLoadFinished: ");
        mPicturesView.setLoadingIndicator(false);

        mCurrentPictures.add(data);
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
