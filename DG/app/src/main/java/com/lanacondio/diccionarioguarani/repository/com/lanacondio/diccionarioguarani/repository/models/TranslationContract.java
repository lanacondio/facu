package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

import android.provider.BaseColumns;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class TranslationContract {
    public static abstract class TranslationEntry implements BaseColumns {
        public static final String TABLE_NAME;

        static {
            TABLE_NAME = "translation";
        }

        public static final String LANGUAGE_ID = "language_id";
        public static final String WORD = "word";
        public static final String TRANSLATION = "translation";
        public static final String CONTEXT = "context";
        public static final String TYPE = "type";

    }
}
