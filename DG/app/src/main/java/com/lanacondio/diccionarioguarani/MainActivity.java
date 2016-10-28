package com.lanacondio.diccionarioguarani;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.PredictiveResultCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private Button allWords;
    private TextView languajeText;
    private TextView tlanguajeText;
    private ListView mResultList;
    private PredictiveResultCursorAdapter mResultAdapter;
    private TranslationDbHelper mTranslationDbHelper;
    private String swtoFind;
    private Integer originalLanguaje = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SearchView wordtf = (SearchView) findViewById(R.id.textToFind);
        wordtf.setIconifiedByDefault(true);
        wordtf.setFocusable(true);
        wordtf.setIconified(false);
        wordtf.requestFocusFromTouch();

            wordtf.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //EditText wordtf = (EditText) findViewById(R.id.textToFind);

                    String valueToFind = query;
                    if(TextUtils.isEmpty(valueToFind)) {

                        return false;
                    }
                    else{
                        Intent allWords = new Intent(MainActivity.this, AllWordsActivity.class);
                        allWords.putExtra("wordtf",valueToFind);
                        allWords.putExtra("olanguage",originalLanguaje);
                        startActivity(allWords);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // you can call or do what you want with your EditText here
                    String valueToFind = newText;
                    swtoFind = valueToFind;

                    if(swtoFind != null && !swtoFind.isEmpty()){

                        mResultList = (ListView) findViewById(R.id.predictiveLV);

                        mResultAdapter = new PredictiveResultCursorAdapter(MainActivity.this, null);

                        // Setup
                        mResultList.setAdapter(mResultAdapter);

                        // Instancia de helper
                        mTranslationDbHelper = new TranslationDbHelper(MainActivity.this);

                        // Carga de datos
                        loadPredictiveResults();

                    }else{
                        mResultList = (ListView) findViewById(R.id.predictiveLV);
                        // Setup
                        mResultList.setAdapter(null);

                    }
                    return true;
                }
            });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_select_language:
                // User chose the "Select  Language" action, mark the current item
                RotateAnimation ra =new RotateAnimation(0, 360);
                ra.setFillAfter(true);
                ra.setDuration(1000);


                final SearchView wordtf = (SearchView) findViewById(R.id.textToFind);
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.mipmap.ic_refresh);
                imageView.startAnimation(ra);


                languajeText = (TextView)findViewById(R.id.originLanguageTextView);
                tlanguajeText = (TextView)findViewById(R.id.translationLanguageTextView);

                final Animation in = new AlphaAnimation(0.0f, 1.0f);
                in.setDuration(1000);

                final Animation out = new AlphaAnimation(1.0f, 0.0f);
                out.setDuration(1000);

                wordtf.setQuery("",false);

                if(languajeText.getText().toString() == getResources().getString(R.string.Guarani)){
                    languajeText.startAnimation(out);
                    languajeText.setText(getResources().getString(R.string.Spanish));
                    languajeText.startAnimation(in);

                    tlanguajeText.startAnimation(out);
                    tlanguajeText.setText(getResources().getString(R.string.Guarani));
                    tlanguajeText.startAnimation(in);

                    originalLanguaje = 2;
                }else{
                    languajeText.startAnimation(out);
                    languajeText.setText(getResources().getString(R.string.Guarani));
                    languajeText.startAnimation(in);

                    tlanguajeText.startAnimation(out);
                    tlanguajeText.setText(getResources().getString(R.string.Spanish));
                    tlanguajeText.startAnimation(in);
                    originalLanguaje = 1;
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    private void loadPredictiveResults() {
        new ResultLoadTask().execute();
    }


    private class ResultLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            if(swtoFind != null && !swtoFind.isEmpty()) {
                return mTranslationDbHelper.getPredictiveTranslations(swtoFind, originalLanguaje);
            }
            else{
                return  null;
            }


        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mResultAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }


}
