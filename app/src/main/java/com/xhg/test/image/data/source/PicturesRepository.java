package com.xhg.test.image.data.source;

import android.content.Context;

/**
 * @author xionghg
 * @created 17-7-20.
 */

public class PicturesRepository {

    private static PicturesRepository sRepository;

    private PicturesRepository() {
    }

    public static PicturesRepository getInstance() {
        if (sRepository == null) {
            synchronized (PicturesRepository.class) {
                if (sRepository == null) {
                    sRepository = new PicturesRepository();
                }
            }
        }
        return sRepository;
    }

    // put here temp
    public static PicturesRepository provideTasksRepository(Context context) {
        return PicturesRepository.getInstance();
    }

    public void refreshPictures() {

    }
}
