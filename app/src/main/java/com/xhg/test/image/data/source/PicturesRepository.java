package com.xhg.test.image.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.source.generate.PicturesGenerateDataSource;
import com.xhg.test.image.data.source.local.PicturesLocalDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.xhg.test.image.utils.CheckUtils.checkNotNull;


/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesRepository implements PicturesDataSource {

    private static PicturesRepository INSTANCE;
    private final PicturesDataSource mPicturesGenerateDataSource;
    private final PicturesDataSource mPicturesLocalDataSource;
    private Map<Integer, Picture> mCachedPictures;
    private boolean mCacheIsDirty;

    private List<PicturesRepositoryObserver> mObservers = new ArrayList<>();
    private List<LoadCallback> mCallbacks = new ArrayList<>();

    private PicturesRepository(PicturesDataSource picturesGenerateDataSource,
                               PicturesDataSource picturesLocalDataSource) {
        mPicturesGenerateDataSource = picturesGenerateDataSource;
        mPicturesLocalDataSource = picturesLocalDataSource;
    }

    // put here temp
    public static PicturesRepository providePicturesRepository(Context context) {
        checkNotNull(context);
        return PicturesRepository.getInstance(PicturesGenerateDataSource.getInstance(),
                PicturesLocalDataSource.getInstance(context));
    }

    public static PicturesRepository getInstance(PicturesDataSource picturesGenerateDataSource,
                                                 PicturesDataSource picturesLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (PicturesRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PicturesRepository(picturesGenerateDataSource,
                            picturesLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public List<Picture> getPictures() {
        List<Picture> pictures = null;

        if (!mCacheIsDirty) {
            // Respond immediately with cache if available and not dirty
            if (mCachedPictures != null) {
                pictures = getCachedPictures();
                return pictures;
            } else {
                // Query the local storage if available.
                pictures = mPicturesLocalDataSource.getPictures();
            }
        }
        // To simplify, we'll consider the local data source fresh when it has data.
        if (pictures == null || pictures.isEmpty()) {
            // Generate pictures if cache is dirty or local data not available.
            pictures = mPicturesGenerateDataSource.getPictures();
            // We copy the data to the device so we don't need to query the network next time
            savePicturesInLocalDataSource(pictures);
        }

        processLoadedPictures(pictures);
        return getCachedPictures();
    }

    public boolean cachedPicturesAvailable() {
        return mCachedPictures != null && !mCacheIsDirty;
    }

    public List<Picture> getCachedPictures() {
        return mCachedPictures == null ? null : new ArrayList<>(mCachedPictures.values());
    }

    private void savePicturesInLocalDataSource(List<Picture> pictures) {
        if (pictures != null) {
            for (Picture Picture : pictures) {
                mPicturesLocalDataSource.savePicture(Picture);
            }
        }
    }

    private void processLoadedPictures(List<Picture> pictures) {
        if (pictures == null) {
            mCachedPictures = null;
            mCacheIsDirty = false;
            return;
        }
        if (mCachedPictures == null) {
            mCachedPictures = new LinkedHashMap<>();
        }
        mCachedPictures.clear();
        for (Picture picture : pictures) {
            mCachedPictures.put(picture.getId(), picture);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void savePicture(@NonNull Picture picture) {
        checkNotNull(picture);
        mPicturesGenerateDataSource.savePicture(picture);
        mPicturesLocalDataSource.savePicture(picture);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedPictures == null) {
            mCachedPictures = new LinkedHashMap<>();
        }
        mCachedPictures.put(picture.getId(), picture);

        // Update the UI
        // notifyContentObserver();
        notifyLoadOneFinished(picture);
    }

    public Picture getPicture(@NonNull final String pictureId) {
        checkNotNull(pictureId);

        Picture cachedPicture = getPictureWithId(pictureId);

        // Respond immediately with cache if we have one
        if (cachedPicture != null) {
            return cachedPicture;
        }

        // Is the picture in the local data source? If not, generate a new one.
        Picture Picture = mPicturesLocalDataSource.getPicture(pictureId);
        if (Picture == null) {
            Picture = mPicturesGenerateDataSource.getPicture(pictureId);
        }

        return Picture;
    }

    @Nullable
    private Picture getPictureWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedPictures == null || mCachedPictures.isEmpty()) {
            return null;
        } else {
            return mCachedPictures.get(id);
        }
    }

    @Override
    public void refreshPictures() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    @Override
    public void deleteAllPictures() {
        mPicturesGenerateDataSource.deleteAllPictures();
        mPicturesLocalDataSource.deleteAllPictures();
        if (mCachedPictures == null) {
            mCachedPictures = new LinkedHashMap<>();
        }
        mCachedPictures.clear();
        // Update the UI
        notifyContentObserver();
    }

    @Override
    public void deletePicture(@NonNull int PictureId) {
        mPicturesGenerateDataSource.deletePicture(checkNotNull(PictureId));
        mPicturesLocalDataSource.deletePicture(checkNotNull(PictureId));
        mCachedPictures.remove(PictureId);
        // Update the UI
        notifyContentObserver();
    }

    public void addContentObserver(PicturesRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(PicturesRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (PicturesRepositoryObserver observer : mObservers) {
            observer.onPicturesChanged();
        }
    }

    public void addLoadCallback(LoadCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    public void removeLoadCallback(LoadCallback callback) {
        if (mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    private void notifyLoadOneFinished(Picture picture) {
        for (LoadCallback callback : mCallbacks) {
            callback.onLoadOneFinished(picture);
        }
    }

    private void notifyRefreshAllFinished(List<Picture> datas) {
        for (LoadCallback callback : mCallbacks) {
            callback.onRefreshAllFinished(datas);
        }
    }

    public interface PicturesRepositoryObserver {
        void onPicturesChanged();
    }

    public interface LoadCallback {
        void onLoadOneFinished(Picture data);
        void onRefreshAllFinished(List<Picture> datas);
    }
}
