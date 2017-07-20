package com.xhg.test.image.data.source.generate;

import android.support.annotation.NonNull;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.source.PicturesDataSource;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class PicturesGenerateDataSource implements PicturesDataSource {

    private static PicturesGenerateDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static Map<String, Picture> PICTURES_SERVICE_DATA;

    public static PicturesGenerateDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PicturesGenerateDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private PicturesGenerateDataSource() {}

    private static void addPicture(String title, String description) {
        Picture newPicture = new Picture(title, description);
        PICTURES_SERVICE_DATA.put(newPicture.getId(), newPicture);
    }

    @Override
    public List<Picture> getPictures() {
        // Simulate network
        try {
            Thread.sleep(SERVICE_LATENCY_IN_MILLIS);
        } catch (InterruptedException e) {

        }
        return (List)PICTURES_SERVICE_DATA.values();
    }

    /**
     * Note: {@link GetPictureCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public Picture getPicture(@NonNull String PictureId) {
        final Picture Picture = PICTURES_SERVICE_DATA.get(PictureId);

        // Simulate network by delaying the execution.
        try {
            Thread.sleep(SERVICE_LATENCY_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Picture;
    }

    @Override
    public void savePicture(@NonNull Picture Picture) {
        PICTURES_SERVICE_DATA.put(Picture.getId(), Picture);
    }

    @Override
    public void refreshPictures() {
        // Not required because the {@link PicturesRepository} handles the logic of refreshing the
        // Pictures from all the available data sources.
    }

    @Override
    public void deleteAllPictures() {
        PICTURES_SERVICE_DATA.clear();
    }

    @Override
    public void deletePicture(@NonNull String PictureId) {
        PICTURES_SERVICE_DATA.remove(PictureId);
    }
}
