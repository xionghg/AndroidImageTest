package com.xhg.test.image.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xhg.test.image.data.Picture;

import java.util.List;

/**
 * Main entry point for accessing Pictures data.
 * <p>
 * For simplicity, only getPictures() and getPicture() have callbacks. Consider adding callbacks to other
 * methods to inform the user of generate/database errors or successful operations.
 * For example, when a new Picture is created, it's synchronously stored in cache but usually every
 * operation on database or generate should be executed in a different thread.
 */
public interface PicturesDataSource {

    interface GetPictureCallback {

        void onPictureLoaded(Picture Picture);

        void onDataNotAvailable();
    }

    @Nullable
    List<Picture> getPictures();

    @Nullable
    Picture getPicture(@NonNull String PictureId);

    void savePicture(@NonNull Picture Picture);

    void refreshPictures();

    void deleteAllPictures();

    void deletePicture(@NonNull int PictureId);
}
