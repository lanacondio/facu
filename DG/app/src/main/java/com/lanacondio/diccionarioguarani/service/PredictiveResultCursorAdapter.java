package com.lanacondio.diccionarioguarani.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.DocumentsContract;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.AllWordsActivity;
import com.lanacondio.diccionarioguarani.MainActivity;
import com.lanacondio.diccionarioguarani.R;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationContract;

import java.util.List;

/**
 * Created by lanacondio on 20/10/2016.
 */


public class PredictiveResultCursorAdapter extends CursorAdapter {

    private Integer originalLanguaje;
    private String wordTF;

    public PredictiveResultCursorAdapter(Context context, Cursor c, String wordToFind) {
        super(context, c, 0);
        wordTF = wordToFind;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.predictive_list_item_word, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        //resultados por item
        Button tword= (Button) view.findViewById(R.id.tvWord);

        String strres = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.WORD));
        originalLanguaje = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.LANGUAGE_ID)));

        String valueToFind = wordTF;
/*
        Integer startIndex = strres.toLowerCase().indexOf(valueToFind.toLowerCase());

        if(startIndex != -1)
        {
            String firstSString = strres.substring(0,startIndex);
            String finalString = strres.substring(startIndex + valueToFind.length(), strres.length());
            tword.setText(this.fromHtml(firstSString + "<font color='#EE0000'>"+valueToFind+"</font>" +  finalString));
        }
        else
        {
            tword.setText(strres);
        }*/

        tword.setText(strres);
        tword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //EditText wordtf = (EditText) findViewById(R.id.textToFind);
                Button tword= (Button) v.findViewById(R.id.tvWord);
                String valueToFind = tword.getText().toString();
                Intent allWords = new Intent((MainActivity)context, AllWordsActivity.class);
                allWords.putExtra("wordtf",valueToFind);
                allWords.putExtra("olanguage",originalLanguaje);
                context.startActivity(allWords);

            }

        });

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
