package com.xhg.test.image.data.source;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.StrategyFactory;
import com.xhg.test.image.strategies.ColorStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesRepository implements PicturesDataSource {

    private static PicturesRepository INSTANCE;
    private SparseArray<Picture> mCachedPictures = new SparseArray<>();

    private PicturesRepository() {
        ColorStrategy[] colorStrategies = StrategyFactory.INSTANCE.getStrategies();
        for (int i = 0; i < colorStrategies.length; i++) {
            mCachedPictures.put(i, new Picture(i, colorStrategies[i]));
        }
    }

    public static PicturesRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (PicturesRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PicturesRepository();
                }
            }
        }
        return INSTANCE;
    }

    public List<Picture> getPictures() {
        List<Picture> pictures = new ArrayList<>();
        for (int i = 0; i < mCachedPictures.size(); i++) {
            pictures.add(mCachedPictures.valueAt(i));
        }
        return pictures;
    }

    @Override
    public void savePicture(@NonNull Picture picture) {
        Objects.requireNonNull(picture);
        mCachedPictures.put(picture.getId(), picture);
    }

    @Override
    public Picture getPicture(final int pictureId) {
        return mCachedPictures.get(pictureId);
    }

    @Override
    public void deleteAllPictures() {
        mCachedPictures.clear();
    }

    @Override
    public void deletePicture(int pictureId) {
        mCachedPictures.remove(pictureId);
    }
}
