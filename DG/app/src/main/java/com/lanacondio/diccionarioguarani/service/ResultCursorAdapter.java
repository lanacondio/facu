package com.lanacondio.diccionarioguarani.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.R;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationContract;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class ResultCursorAdapter  extends CursorAdapter {
    private Map<String, Translation> translationsList = new HashMap<String, Translation>();
    public ResultCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    private TranslationDbHelper mTranslationDbHelper;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_word, viewGroup, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        //resultados

        mTranslationDbHelper = new TranslationDbHelper(context);
        final int position = cursor.getPosition()+1;

        TextView tIndex= (TextView) view.findViewById(R.id.tvIndex);
        //TextView ttype= (TextView) view.findViewById(R.id.tvType);
        final TextView ttranslation = (TextView) view.findViewById(R.id.tvTranslation);
        TextView tcontext = (TextView) view.findViewById(R.id.tvContext);
        FloatingActionButton delButton  = (FloatingActionButton)view.findViewById(R.id.delword);

        String strres = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TRANSLATION));
        String contexts = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.CONTEXT));
        String type = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TYPE));
        String gcmid = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.GCM_ID));

        String wordtf = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.WORD));
        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry._ID)));
        String swebid = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.WEB_ID));
        int webid = 0;
        if(swebid!=null){
            webid = Integer.parseInt(swebid);
        }

        int languageId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.LANGUAGE_ID)));

        Translation ttadd = new Translation();

        ttadd.setId(id);
        ttadd.setWebId(webid);
        ttadd.setCountryId(languageId);
        ttadd.setWordToFind(wordtf);
        ttadd.setTranslationResult(strres);
        ttadd.setContext(contexts);
        ttadd.setType(type);
        ttadd.setDeviceId(gcmid);
        translationsList.put(strres, ttadd);

        ttranslation.setText(strres);
        //ttype.setText(type);
        tcontext.setText(contexts);
        if(strres != context.getString(R.string.not_found_word))
        {
            tIndex.setText(String.valueOf(position)+".");
        }


        if(webid != 0){
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    Translation ttpost = translationsList.get(ttranslation.getText().toString());
                    mTranslationDbHelper.delTranslation(ttpost);
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = context.getString(R.string.delete_word_text);
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

        }
        else {
            delButton.setVisibility(View.INVISIBLE);
        }


    }
}
