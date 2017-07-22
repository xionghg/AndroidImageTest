package com.xhg.test.image.pictures;

import android.support.annotation.NonNull;

import com.xhg.test.image.BasePresenter;
import com.xhg.test.image.BaseView;
import com.xhg.test.image.data.Picture;

import java.util.List;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-19.
 */

public interface PicturesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showPictures(List<Picture> pictures);

        void showPictureUpdate(int index, Picture picture);

        void showEmptyPictures(int size);

        void showPictureDetailsUi(String pictureId);

        void showLoadingPicturesError();

        void showNoPictures();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadPictures(boolean forceUpdate);

        void openPictureDetails(@NonNull Picture requestedPicture);

        void registerRepositoryCallBack(boolean isRegister);
    }
}
