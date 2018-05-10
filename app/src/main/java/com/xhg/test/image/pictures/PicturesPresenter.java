package com.xhg.test.image.pictures;

import android.support.annotation.NonNull;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.StrategyFactory;
import com.xhg.test.image.data.source.PicturesRepository;
import com.xhg.test.image.strategies.BitmapGenerator;
import com.xhg.test.image.strategies.ColorStrategy;
import com.xhg.test.image.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesPresenter implements PicturesContract.Presenter,
        PicturesRepository.LoadCallback {
    private static final String TAG = "PicturesPresenter";

    private final PicturesRepository mPicturesRepository;

    private final PicturesContract.View mPicturesView;

    private List<Picture> mCurrentPictures;
    private List<BitmapGenerator> mBitmapGenerators = new ArrayList<>();

    private boolean mFirstLoad;

    public PicturesPresenter(@NonNull PicturesRepository PicturesRepository,
                             @NonNull PicturesContract.View PicturesView) {
        mPicturesRepository = Objects.requireNonNull(PicturesRepository, "PicturesRepository cannot be null");
        mPicturesView = Objects.requireNonNull(PicturesView, "PicturesView cannot be null!");
        mPicturesView.setPresenter(this);
    }

    @Override
    public void start() {
        // mCurrentPictures = mPicturesRepository.getPictures();
        mCurrentPictures = new ArrayList<>();
        ColorStrategy[] colorStrategies = StrategyFactory.INSTANCE.getStrategies();
        int n = 0;
        for (ColorStrategy strategy : colorStrategies) {
            Picture pic = new Picture(n++, strategy);
            mCurrentPictures.add(pic);
        }
        mPicturesView.showPictures(mCurrentPictures);

        //startGenerateColor();
    }

    public void startGenerateColor() {
        if (mBitmapGenerators.isEmpty()) {
            for (int i = 0; i < 5/*mCurrentPictures.size()*/; i++) {
                final int index = i;
                Log.e(TAG, "start generator" + i);
                BitmapGenerator generator = BitmapGenerator.with(bitmap -> {
                            mCurrentPictures.get(index).setBitmap(bitmap);
                            mPicturesView.showPictureUpdate(index, mCurrentPictures.get(index));
                        })
                        .setWidth(256)
                        .setHeight(256)
                        .setStrategy(mCurrentPictures.get(index).getStrategy());
                mBitmapGenerators.add(generator);
            }
        }

        for (BitmapGenerator generator : mBitmapGenerators) {
            generator.startInParallel();
        }
    }

    @Override
    public void cancelCurrentOperation() {
        Log.d(TAG, "cancelCurrentOperation: ");
        for (BitmapGenerator generator : mBitmapGenerators) {
            generator.cancel();
        }
    }

    @Override
    public void result(int requestCode, int resultCode) {
    }

    private void showFilteredPictures() {
        List<Picture> picturesToDisplay = new ArrayList<>();
        if (mCurrentPictures != null) {
            for (Picture Picture : mCurrentPictures) {
                // TODO: add some filters
                picturesToDisplay.add(Picture);
            }
        }
        processPictures(picturesToDisplay);
    }

    public void loadPictures(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad) {
            mFirstLoad = false;
            mPicturesRepository.refreshPictures();
        } else {
            showFilteredPictures();
            mPicturesView.setLoadingIndicator(false);
        }
    }

    @Override
    public void registerRepositoryCallBack(boolean isRegister) {
        if (isRegister) {
            mPicturesRepository.addLoadCallback(this);
        } else {
            mPicturesRepository.removeLoadCallback(this);
        }
    }

    private void processPictures(List<Picture> pictures) {
        if (pictures.isEmpty()) {
            // Show a message indicating there are no pictures.
            processEmptyPictures();
        } else {
            // Show the list of pictures
            mPicturesView.showPictures(pictures);
        }
    }

    private void processEmptyPictures() {
        mPicturesView.showNoPictures();
    }

    @Override
    public void openPictureDetails(@NonNull Picture requestedPicture) {
        Objects.requireNonNull(requestedPicture, "requestedPicture cannot be null!");
        mPicturesView.showPictureDetailsUi(requestedPicture.getId());
    }

    @Override
    public void onLoadOneFinished(Picture data) {

    }

    @Override
    public void onRefreshAllFinished(List<Picture> datas) {

    }
}
