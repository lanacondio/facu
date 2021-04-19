package com.lanacondio.diccionarioguarani.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
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
        TextView wtf = (TextView)  view.findViewById(R.id.tvWordText);

        String strres = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.WORD));
        String wtftranslationres = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TRANSLATION));
        Spannable spanString = SpannableString.valueOf(strres);
        originalLanguaje = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.LANGUAGE_ID)));

        String valueToFind = wordTF;
        strres = strres.replace("-","");
        String showstrres = strres +": ";
        if(wtftranslationres.length() <= 20){
            showstrres +=  wtftranslationres;
        }else {
            showstrres +=  wtftranslationres.substring(0,20);
        }

        tword.setText("Ver más de: "+strres, TextView.BufferType.SPANNABLE);
        wtf.setText(showstrres, TextView.BufferType.SPANNABLE);

        tword.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //EditText wordtf = (EditText) findViewById(R.id.textToFind);
                    Button tword = (Button) v.findViewById(R.id.tvWord);
                    String valueToFind = tword.getText().toString().replace("Ver más de: ","");
                    Intent allWords = new Intent((MainActivity) context, AllWordsActivity.class);
                    allWords.putExtra("wordtf", valueToFind);
                    allWords.putExtra("olanguage", originalLanguaje);
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
