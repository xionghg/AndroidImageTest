package com.xhg.test.image.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the Pictures locally.
 */
public final class PicturesPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PicturesPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PictureEntry implements BaseColumns {
        public static final String TABLE_NAME = "Picture";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
