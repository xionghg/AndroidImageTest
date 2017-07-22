package com.xhg.test.image.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.xhg.test.image.data.Picture;
import com.xhg.test.image.data.source.PicturesDataSource;
import com.xhg.test.image.data.source.local.PicturesPersistenceContract.PictureEntry;

import java.util.ArrayList;
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

    private SQLiteDatabase mDb;

    // Prevent direct instantiation.
    private PicturesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new PicturesDbHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public static PicturesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PicturesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Picture> getPictures() {
        List<Picture> Pictures = new ArrayList<Picture>();
        try {
            String[] projection = {
                    PictureEntry.COLUMN_NAME_ENTRY_ID,
                    PictureEntry.COLUMN_NAME_TITLE,
                    PictureEntry.COLUMN_NAME_DESCRIPTION,
            };

            Cursor c = mDb.query(
                    PictureEntry.TABLE_NAME, projection, null, null, null, null, null);

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String itemId = c
                            .getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_ENTRY_ID));
                    String title = c
                            .getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_TITLE));
                    Picture Picture = new Picture(itemId, title);
                    Pictures.add(Picture);
                }
            }
            if (c != null) {
                c.close();
            }

        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
        return Pictures;
    }

    /**
     * Note: {@link GetPictureCallback#onDataNotAvailable()} is fired if the {@link Picture} isn't
     * found.
     */
    @Override
    public Picture getPicture(@NonNull String PictureId) {
        try {
            String[] projection = {
                    PictureEntry.COLUMN_NAME_ENTRY_ID,
                    PictureEntry.COLUMN_NAME_TITLE,
                    PictureEntry.COLUMN_NAME_DESCRIPTION,
            };

            String selection = PictureEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {PictureId};

            Cursor c = mDb.query(
                    PictureEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

            Picture Picture = null;

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                String itemId = c
                        .getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(PictureEntry.COLUMN_NAME_TITLE));
                Picture = new Picture(itemId, title);
            }
            if (c != null) {
                c.close();
            }

            return Picture;
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
        return null;
    }

    @Override
    public void savePicture(@NonNull Picture Picture) {
        try {
            checkNotNull(Picture);

            ContentValues values = new ContentValues();
            values.put(PictureEntry.COLUMN_NAME_ENTRY_ID, Picture.getId());
            values.put(PictureEntry.COLUMN_NAME_TITLE, Picture.getTitle());

            mDb.insert(PictureEntry.TABLE_NAME, null, values);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void refreshPictures() {
        // Not required because the {@link PicturesRepository} handles the logic of refreshing the
        // Pictures from all the available data sources.
    }

    @Override
    public void deleteAllPictures() {
        try {
            mDb.delete(PictureEntry.TABLE_NAME, null, null);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }

    @Override
    public void deletePicture(@NonNull String PictureId) {
        try {
            String selection = PictureEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {PictureId};

            mDb.delete(PictureEntry.TABLE_NAME, selection, selectionArgs);
        } catch (IllegalStateException e) {
            // Send to analytics, log etc
        }
    }
}
