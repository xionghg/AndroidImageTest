package com.xhg.test.image.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xhg.test.image.data.Picture;

import java.util.List;

public interface PicturesDataSource {

    List<Picture> getPictures();

    @Nullable
    Picture getPicture(int pictureId);

    void savePicture(@NonNull Picture Picture);

    void deleteAllPictures();

    void deletePicture(int pictureId);
}
