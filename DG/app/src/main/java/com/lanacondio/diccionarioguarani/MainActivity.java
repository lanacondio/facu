package com.lanacondio.diccionarioguarani;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.PredictiveResultCursorAdapter;

import java.util.List;

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
        getTheme().applyStyle(new Preferences(this).getFontStyle().getResId(), true);
        getTheme().applyStyle(new Preferences(this).getFontColor().getResId(), true);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.mipmap.ic_bandera);

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
        Context context = getApplicationContext();
        CharSequence text = "app compartida";
        int duration = Toast.LENGTH_SHORT;
        switch (item.getItemId()) {

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

            case  R.id.favorites:

                Intent favorites = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(favorites);

                return true;

            case  R.id.Share_app:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String shareBody =  "";

                shareBody += context.getString(R.string.share_app_messages)+context.getPackageName();

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;


            case  R.id.calificate_app:

                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
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
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }

                return true;

            case  R.id.about_app:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("gdicc es una app comunitaria sin fines de lucro. Todos los derechos reservados.")
                        .setTitle("Acerca de gdicc")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();

                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(final DialogInterface dialog) {
                                                Button buttonbackground = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                                                buttonbackground.setTextColor(Color.BLUE);

                                            }
                                        });

                alert.show();
                return true;

            case R.id.configuration:

                Intent configuration = new Intent(MainActivity.this, ConfigurationActivity.class);
                startActivity(configuration);

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
