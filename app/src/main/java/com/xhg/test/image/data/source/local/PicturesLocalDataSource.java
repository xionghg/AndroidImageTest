package com.xhg.test.image.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.source.PicturesDataSource;

import java.util.List;

import static com.xhg.test.image.utils.CheckUtils.checkNotNull;


/**
 * Concrete implementation of a data source as a db.
 * <P>
 * Note: this is a singleton and we are opening the database once and not closing it. The framework
 * cleans up the resources when the application closes so we don't need to close the db.
 */
public class PicturesLocalDataSource implements PicturesDataSource {

    private static PicturesLocalDataSource INSTANCE;

    private PicturesDbHelper mDbHelper;

    // Prevent direct instantiation.
    private PicturesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new PicturesDbHelper(context);
    }

    public static PicturesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PicturesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Picture> getPictures() {
        // eg: get pictures form sd card
        return null;
    }

    /**
     * Note: {@link GetPictureCallback#onDataNotAvailable()} is fired if the {@link Picture} isn't
     * found.
     */
    @Override
    public Picture getPicture(@NonNull String PictureId) {
        // TODO: get picture form sd card
        return null;
    }

    @Override
    public void savePicture(@NonNull Picture Picture) {
        // TODO: save picture to sd card
    }

    @Override
    public void refreshPictures() {
        // Not required because the {@link PicturesRepository} handles the logic of refreshing the
        // Pictures from all the available data sources.
    }

    @Override
    public void deleteAllPictures() {

    }

    @Override
    public void deletePicture(@NonNull int PictureId) {

    }
}
