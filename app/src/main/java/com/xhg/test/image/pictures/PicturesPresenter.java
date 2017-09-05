package com.xhg.test.image.pictures;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.StrategyFactory;
import com.xhg.test.image.data.source.PicturesRepository;
import com.xhg.test.image.strategies.ColorGenerator;
import com.xhg.test.image.strategies.ColorStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.xhg.test.image.utils.CheckUtils.checkNotNull;


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

    private boolean mFirstLoad;

    public PicturesPresenter(@NonNull PicturesRepository PicturesRepository,
                             @NonNull PicturesContract.View PicturesView) {
        mPicturesRepository = checkNotNull(PicturesRepository, "PicturesRepository cannot be null");
        mPicturesView = checkNotNull(PicturesView, "PicturesView cannot be null!");
        mPicturesView.setPresenter(this);
    }

    @Override
    public void start() {
        // mCurrentPictures = mPicturesRepository.getPictures();
        mCurrentPictures = new ArrayList<>();
        ColorStrategy[] colorStrategies = StrategyFactory.getInstance().getStrategies();
        int n = 0;
        for (ColorStrategy strategy : colorStrategies) {
            Picture pic = new Picture(n++, strategy);
            mCurrentPictures.add(pic);
        }
        mPicturesView.showEmptyPictures(mCurrentPictures);

//        startGenerateColor();
    }

    private void startGenerateColor() {
        for (int i = 0; i < mCurrentPictures.size(); i++) {
            final int index = i;
            ColorGenerator.Callback callback = new ColorGenerator.SimpleCallback() {
                @Override
                public void onColorsCreated(Bitmap bitmap) {
                    mCurrentPictures.get(index).setBitmap(bitmap);
                    mPicturesView.showPictureUpdate(index, mCurrentPictures.get(index));
                }
            };
            Log.e(TAG, "start generator"+i);
            new ColorGenerator.Builder(callback)
                    .setWidth(512)
                    .setHeight(512)
                    .setColorStrategy(mCurrentPictures.get(index).getStrategy())
                    .build()
                    .startInParallel();
        }
    }

    @Override
    public void result(int requestCode, int resultCode) {

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

    @Override
    public void registerRepositoryCallBack(boolean isRegister) {
        if (isRegister) {
            mPicturesRepository.addLoadCallback(this);
        } else {
            mPicturesRepository.removeLoadCallback(this);
        }
    }

    private void processPictures(List<Picture> Pictures) {
        if (Pictures.isEmpty()) {
            // Show a message indicating there are no Pictures.
            processEmptyPictures();
        } else {
            // Show the list of Pictures
            mPicturesView.showEmptyPictures(Pictures);
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

    @Override
    public void onLoadOneFinished(Picture data) {

    }

    @Override
    public void onRefreshAllFinished(List<Picture> datas) {

    }
}
