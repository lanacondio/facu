package com.lanacondio.diccionarioguarani.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;

import com.lanacondio.diccionarioguarani.AllWordsActivity;
import com.lanacondio.diccionarioguarani.FavoritesActivity;
import com.lanacondio.diccionarioguarani.MainActivity;
import com.lanacondio.diccionarioguarani.R;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.FavoriteContract;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.FavoriteDbHelper;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationContract;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanacondio on 20/10/2016.
 */


public class FavoritesCursorAdapter extends CursorAdapter {

    private Map<String, Integer> originalLanguajes = new HashMap<String, Integer>();

    public FavoritesCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.favorite_list_item_word, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        //resultados por item
        final Button tword= (Button) view.findViewById(R.id.tvWord);

        FloatingActionButton sharingb = (FloatingActionButton)view.findViewById(R.id.share_favorite);

        FloatingActionButton addFavorite  = (FloatingActionButton)view.findViewById(R.id.set_favorite_status);

        FloatingActionButton gotodefinition  = (FloatingActionButton)view.findViewById(R.id.go_definition);

        final String strres = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.WORD));
        final int idFav = Integer.parseInt( cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry._ID)));
        Integer originalLanguaje = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.LANGUAGE_ID)));
        originalLanguajes.put(strres, originalLanguaje);
        tword.setText(strres);
        tword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //EditText wordtf = (EditText) findViewById(R.id.textToFind);
                Button tword= (Button) v.findViewById(R.id.tvWord);
                String valueToFind = tword.getText().toString();
                Intent allWords = new Intent((FavoritesActivity)context, AllWordsActivity.class);
                allWords.putExtra("wordtf",valueToFind);
                Integer originalLanguaje = originalLanguajes.get(valueToFind);
                allWords.putExtra("olanguage",originalLanguaje);
                context.startActivity(allWords);

            }

        });

        sharingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String wtoFind = strres;
                TranslationDbHelper mTranslationDbHelper = new TranslationDbHelper(context) ;
                Integer originalLanguaje = originalLanguajes.get(wtoFind);
                List<Translation> results =  mTranslationDbHelper.getTranslationsObject(wtoFind,originalLanguaje);
                String shareBody =  "";
                int index = 1;
                for (Translation translation: results
                        ) {

                    shareBody += String.format(context.getString(R.string.share_messages_items), translation.getWordToFind(),
                            String.valueOf(index), translation.getTranslationResult(), translation.getContext());

                    index++;

                }

                shareBody += context.getString(R.string.share_messages_tag);

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        gotodefinition.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Intent allWords = new Intent((FavoritesActivity)context, AllWordsActivity.class);
                allWords.putExtra("wordtf",strres);
                Integer originalLanguaje = originalLanguajes.get(strres);
                allWords.putExtra("olanguage",originalLanguaje);
                context.startActivity(allWords);

            }

        });

        addFavorite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                FavoriteDbHelper mFavoriteDbHelper = new FavoriteDbHelper(context) ;
                FloatingActionButton addFavorite  = (FloatingActionButton)v.findViewById(R.id.set_favorite_status);

                //EditText wordtf = (EditText) findViewById(R.id.textToFind);
                if(mFavoriteDbHelper.isFavorite(idFav))
                {
                    //si esta elimino
                    mFavoriteDbHelper.delFavorites(idFav);
                    addFavorite.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_white_18dp));
                }
                else
                {
                    //inserto si no esta
                    String wtoFind = strres;
                    Integer originalLanguaje = originalLanguajes.get(strres);
                    mFavoriteDbHelper.addFavorites(wtoFind,originalLanguaje);
                    addFavorite.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_favorite_black_24dp));

                }


            }

        });


    }
}
