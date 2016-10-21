package com.lanacondio.diccionarioguarani.service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.R;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class ResultCursorAdapter  extends CursorAdapter {

    public ResultCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_word, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        //resultados
        TextView ttype= (TextView) view.findViewById(R.id.tvType);
        TextView ttranslation = (TextView) view.findViewById(R.id.tvTranslation);
        TextView tcontext = (TextView) view.findViewById(R.id.tvContext);

        //foreach definition...
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            //cursor is empty
            ttranslation.setText("No se encontraron resultados");
        }
        else{

            do {
                Translation translate = new Translation();
                String strres = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TRANSLATION));
                String contexts = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.CONTEXT));
                String type = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TYPE));

                ttranslation.setText(strres);
                ttype.setText(type);
                tcontext.setText(contexts);
            } while (cursor.moveToNext());

        }
    }
}
