package com.lanacondio.diccionarioguarani;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;

import java.util.List;

public class AllWordsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_words);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton sharingb = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        FloatingActionButton previousb = (FloatingActionButton)findViewById(R.id.previousActionButton);
        FloatingActionButton copyText  = (FloatingActionButton)findViewById(R.id.copyToClipboardActionButton);

        sharingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                Resources res = getResources();

                Context context = getApplicationContext();
                String wtoFind = getIntent().getStringExtra("wordtf");
                int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                TranslationDbHelper mTranslationDbHelper = new TranslationDbHelper(AllWordsActivity.this) ;
                List<Translation> results =  mTranslationDbHelper.getTranslationsObject(wtoFind,originalLanguaje);
                String shareBody =  "";
                int index = 1;
                for (Translation translation: results
                     ) {
                   // shareBody += String.format(res.getString(R.string.share_messages_items), translation.getWordToFind(),
                   //         index, translation.getTranslationResult(), translation.getContext());


                    shareBody += String.format(context.getString(R.string.share_messages_items), translation.getWordToFind(),
                                    String.valueOf(index), translation.getTranslationResult(), translation.getContext());

                    //shareBody += " "+translation.getWordToFind()+":"+" "+String.valueOf(index)+
                    //"."+" "+translation.getTranslationResult()+"\n" + translation.getContext() +"\n";

                }

                shareBody += context.getString(R.string.share_messages_tag);

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        previousb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllWordsActivity.super.onBackPressed();
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

    public void OnFragmentInteractionListener(Uri uri){
        //you can leave it empty
    }

}
