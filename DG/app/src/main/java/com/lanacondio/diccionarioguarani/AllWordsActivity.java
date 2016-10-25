package com.lanacondio.diccionarioguarani;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AllWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_words);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton sharingb = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        sharingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
