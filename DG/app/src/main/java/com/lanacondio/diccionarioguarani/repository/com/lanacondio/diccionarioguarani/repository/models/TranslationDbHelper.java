package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class TranslationDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Translation.db";

    public TranslationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL

        sqLiteDatabase.execSQL("CREATE TABLE " + TranslationContract.TranslationEntry.TABLE_NAME + " ("
                + TranslationContract.TranslationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TranslationContract.TranslationEntry.LANGUAGE_ID + " INTEGER NOT NULL,"
                + TranslationContract.TranslationEntry.WORD + " TEXT NOT NULL,"
                + TranslationContract.TranslationEntry.TRANSLATION + " TEXT NOT NULL,"
                + TranslationContract.TranslationEntry.TYPE + " TEXT NOT NULL,"
                + TranslationContract.TranslationEntry.CONTEXT + " TEXT NOT NULL,"
                + "UNIQUE (" + TranslationContract.TranslationEntry._ID + "))");


        ContentValues values = new ContentValues();
        //leer datos de archivo de texto e insertarlos con un for

        // Pares clave-valor
        values.put(TranslationContract.TranslationEntry.LANGUAGE_ID, "1");
        values.put(TranslationContract.TranslationEntry.WORD, "a");
        values.put(TranslationContract.TranslationEntry.TRANSLATION, "lugar");
        values.put(TranslationContract.TranslationEntry.TYPE, "sust.");
        values.put(TranslationContract.TranslationEntry.CONTEXT, "en el LUGAR");

        sqLiteDatabase.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values);

        ContentValues values2 = new ContentValues();

        values2.put(TranslationContract.TranslationEntry.LANGUAGE_ID, "1");
        values2.put(TranslationContract.TranslationEntry.WORD, "ahy´o");
        values2.put(TranslationContract.TranslationEntry.TRANSLATION, "garganta");
        values2.put(TranslationContract.TranslationEntry.TYPE, "sust.");
        values2.put(TranslationContract.TranslationEntry.CONTEXT, "en la GARGANTA");

        sqLiteDatabase.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values2);

        ContentValues values3 = new ContentValues();

        values3.put(TranslationContract.TranslationEntry.LANGUAGE_ID, "1");
        values3.put(TranslationContract.TranslationEntry.WORD, "aipo");
        values3.put(TranslationContract.TranslationEntry.TRANSLATION, "aquel");
        values3.put(TranslationContract.TranslationEntry.TYPE, "sust.");
        values3.put(TranslationContract.TranslationEntry.CONTEXT, "en AQUEL lugar");

        sqLiteDatabase.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values3);

        ContentValues values4 = new ContentValues();

        values4.put(TranslationContract.TranslationEntry.LANGUAGE_ID, "1");
        values4.put(TranslationContract.TranslationEntry.WORD, "aja");
        values4.put(TranslationContract.TranslationEntry.TRANSLATION, "durante");
        values4.put(TranslationContract.TranslationEntry.TYPE, "sust.");
        values4.put(TranslationContract.TranslationEntry.CONTEXT, "sucedió DURANTE el juego");

        sqLiteDatabase.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values4);

        ContentValues values5 = new ContentValues();


        values5.put(TranslationContract.TranslationEntry.LANGUAGE_ID, "1");
        values5.put(TranslationContract.TranslationEntry.WORD, "alemaniagua");
        values5.put(TranslationContract.TranslationEntry.TRANSLATION, "aleman");
        values5.put(TranslationContract.TranslationEntry.TYPE, "gent.");
        values5.put(TranslationContract.TranslationEntry.CONTEXT, "el señor es ALEMÁN");

        sqLiteDatabase.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values5);

        ContentValues values6 = new ContentValues();

        values6.put(TranslationContract.TranslationEntry.LANGUAGE_ID, "1");
        values6.put(TranslationContract.TranslationEntry.WORD, "alkila");
        values6.put(TranslationContract.TranslationEntry.TRANSLATION, "alquilar");
        values6.put(TranslationContract.TranslationEntry.TYPE, "ver.");
        values6.put(TranslationContract.TranslationEntry.CONTEXT, "debemos ALQUILAR un lugar");

        // Insertar...
        sqLiteDatabase.insert(TranslationContract.TranslationEntry.TABLE_NAME, null, values6);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public Cursor getTranslations(String word)
    {
        Cursor c = getReadableDatabase().query(
                TranslationContract.TranslationEntry.TABLE_NAME,
                null,
                TranslationContract.TranslationEntry.WORD + " LIKE ?",
                new String[]{word},
                null,
                null,
                null);
        return c;

    }

    public Cursor getPredictiveTranslations(String word)
    {
        Cursor c = getReadableDatabase().query(
                TranslationContract.TranslationEntry.TABLE_NAME,
                null,
                TranslationContract.TranslationEntry.WORD + " LIKE ?",
                new String[]{"%"+word+"%"},
                null,
                null,
                null);
        return c;

    }


    public Cursor getAllWords() {
        return getReadableDatabase()
                .query(
                        TranslationContract.TranslationEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

}
