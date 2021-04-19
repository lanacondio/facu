package com.lanacondio.diccionarioguarani;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.FavoriteDbHelper;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.GdiccApiClient;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class AllWordsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(new Preferences(this).getFontStyle().getResId(), true);
        setContentView(R.layout.activity_all_words);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_all_words);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton sharingb = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        FloatingActionButton copyText  = (FloatingActionButton)findViewById(R.id.copyToClipboardActionButton);

        FloatingActionButton addFavorite  = (FloatingActionButton)findViewById(R.id.addFavorite);

        //Button allWordsWeb = (Button) findViewById(R.id.webRequestButton);

        FavoriteDbHelper mFavoriteDbHelper = new FavoriteDbHelper(AllWordsActivity.this) ;
        String wtoFind = getIntent().getStringExtra("wordtf");

        if(mFavoriteDbHelper.isFavorite(wtoFind))
        {
            addFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_favorite_black_24dp));
        }


        sharingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                Context context = getApplicationContext();
                String wtoFind = getIntent().getStringExtra("wordtf");
                int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                TranslationDbHelper mTranslationDbHelper = new TranslationDbHelper(AllWordsActivity.this) ;
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
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                String wtoFind = getIntent().getStringExtra("wordtf");
                int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                TranslationDbHelper mTranslationDbHelper = new TranslationDbHelper(AllWordsActivity.this) ;
                List<Translation> results =  mTranslationDbHelper.getTranslationsObject(wtoFind,originalLanguaje);
                String textBody =  "";
                int index = 1;
                for (Translation translation: results
                        ) {

                    textBody += translation.getWordToFind()+":"+" "+String.valueOf(index)+
                            "."+" "+translation.getTranslationResult()+"\n" + translation.getContext() +"\n";
                    index++;

                }

                ClipData clip = ClipData.newPlainText(textBody,textBody);
                clipboard.setPrimaryClip(clip);
                Context context = getApplicationContext();
                CharSequence text = context.getString(R.string.copy_to_clipboard);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteDbHelper mFavoriteDbHelper = new FavoriteDbHelper(AllWordsActivity.this) ;
                String wtoFind = getIntent().getStringExtra("wordtf");

                if(mFavoriteDbHelper.isFavorite(wtoFind))
                {
                    int originalLanguaje = getIntent().getIntExtra("olanguage",1);

                    mFavoriteDbHelper.delFavorites(wtoFind,originalLanguaje);
                    FloatingActionButton addFavorite  = (FloatingActionButton)findViewById(R.id.addFavorite);
                    addFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_favorite_white_18dp));
                }
                else
                {
                    int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                    mFavoriteDbHelper.addFavorites(wtoFind,originalLanguaje);
                    FloatingActionButton addFavorite  = (FloatingActionButton)findViewById(R.id.addFavorite);
                    addFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_favorite_black_24dp));

                }


            }
        });

        /*
        allWordsWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String wtoFind = getIntent().getStringExtra("wordtf");
                int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                Intent allWordsWeb = new Intent(AllWordsActivity.this, AllWordsWebActivity.class);
                allWordsWeb.putExtra("wordtf",wtoFind);
                allWordsWeb.putExtra("olanguage",originalLanguaje);
                startActivity(allWordsWeb);

            }
        });*/

        AllWordsFragment fragment = (AllWordsFragment)
        getSupportFragmentManager().findFragmentById(R.id.content_all_words);

        if (fragment == null) {
            fragment = AllWordsFragment.newInstance();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_all_words, fragment)
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_words,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Context context = getApplicationContext();
        switch (item.getItemId()) {

            case android.R.id.home:
                AllWordsActivity.super.onBackPressed();

                return true;

            case  R.id.favorites:

                Intent favorites = new Intent(AllWordsActivity.this, FavoritesActivity.class);
                startActivity(favorites);

                return true;
            case  R.id.Share_app:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String shareBody =  "";

                shareBody += context.getString(R.string.share_app_messages) +getApplicationContext().getPackageName();

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case  R.id.calificate_app:

                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }




}
