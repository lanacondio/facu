package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

import android.provider.BaseColumns;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class FavoriteContract {
    public static abstract class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME;

        static {
            TABLE_NAME = "favorite";
        }

        public static final String LANGUAGE_ID = "language_id";
        public static final String WORD = "word";

    }
}
