package com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Favorite.db";

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME + " ("
                + FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteContract.FavoriteEntry.LANGUAGE_ID + " INTEGER NOT NULL,"
                + FavoriteContract.FavoriteEntry.WORD + " TEXT NOT NULL,"
                + "UNIQUE (" + FavoriteContract.FavoriteEntry._ID + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public Cursor getFavorites()
    {
        return getReadableDatabase()
            .query(
                    FavoriteContract.FavoriteEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
    }

    public Cursor getPredictiveFavorites(String wordtf){

        Cursor c = getReadableDatabase().query(true,
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                new String[]{"_id",FavoriteContract.FavoriteEntry.WORD,
                        FavoriteContract.FavoriteEntry.LANGUAGE_ID,
                        FavoriteContract.FavoriteEntry.WORD
                },
                TranslationContract.TranslationEntry.WORD + " LIKE ? ",
                new String[]{"%"+wordtf+"%"},
                FavoriteContract.FavoriteEntry.WORD,
                null,
                null,
                null);

        return c;
    }

    public Boolean addFavorites(String word, int languajeId)
    {
        if(!isFavorite(word))
        {
            try
            {
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put(FavoriteContract.FavoriteEntry.LANGUAGE_ID, languajeId);
                nuevoRegistro.put(FavoriteContract.FavoriteEntry.WORD,word);

                getReadableDatabase().insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, nuevoRegistro);

                return true;

            }
            catch (Exception ex)
            {
                return false;
            }

        }
        return false;

    }

    public Boolean delFavorites(int idFav)
    {
        try
        {
            getReadableDatabase().delete(FavoriteContract.FavoriteEntry.TABLE_NAME,FavoriteContract.FavoriteEntry._ID + "=" + idFav, null);
            return true;

        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public Boolean delFavorites(String wordtf, int olanguage)
    {
        try
        {
            getReadableDatabase().delete(FavoriteContract.FavoriteEntry.TABLE_NAME,FavoriteContract.FavoriteEntry.WORD + " LIKE  '" + wordtf + "' and "
                    +FavoriteContract.FavoriteEntry.LANGUAGE_ID +" = "+olanguage, null);
            return true;

        }
        catch (Exception ex)
        {
            return false;
        }
    }


    public Boolean isFavorite(int idFav)
    {
        try
        {
           Cursor cursor = getReadableDatabase().rawQuery("SELECT "+ FavoriteContract.FavoriteEntry._ID
                   +" FROM "+ FavoriteContract.FavoriteEntry.TABLE_NAME+" WHERE "+ FavoriteContract.FavoriteEntry._ID +"=?", new String[] {idFav + ""});

            return cursor.getCount() > 0;
        }
        catch (Exception ex)
        {
            return false;
        }

    }

    public Boolean isFavorite(String wordtf)
    {
        try
        {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT "+ FavoriteContract.FavoriteEntry._ID
                    +" FROM "+ FavoriteContract.FavoriteEntry.TABLE_NAME+" WHERE "+ FavoriteContract.FavoriteEntry.WORD +" LIKE ?", new String[] {wordtf + ""});

            return cursor.getCount() > 0;
        }
        catch (Exception ex)
        {
            return false;
        }

    }


}
