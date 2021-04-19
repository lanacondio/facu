package com.lanacondio.diccionarioguarani;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
/*
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
*/
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.AbbreviationSupportHelper;
import com.lanacondio.diccionarioguarani.service.PredictiveResultCursorAdapter;
import com.lanacondio.diccionarioguarani.service.Session;

public class MainActivity extends AppCompatActivity {

    private Button allWords;
    private TextView languajeText;
    private TextView tlanguajeText;
    private ListView mResultList;
    private PredictiveResultCursorAdapter mResultAdapter;
    private TranslationDbHelper mTranslationDbHelper;
    private String swtoFind;
    private Integer originalLanguaje = 1;
    private RelativeLayout popupRelativeLayout;
    private PopupWindow mPopupWindow;
    private AdView mAdView;
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Session session = new Session(getApplicationContext());
        session.createLoginSession();

        if(session.showAbbreviationHelp()){
            AbbreviationSupportHelper abbHelper = new AbbreviationSupportHelper();
            abbHelper.IntroSupport(this);
        }


        wordtf.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //EditText wordtf = (EditText) findViewById(R.id.textToFind);
                    query= query.replace("'","´");
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
                    valueToFind = valueToFind.replace("'","´");

                    swtoFind = valueToFind;

                    if(swtoFind != null && !swtoFind.isEmpty()){

                        mResultList = (ListView) findViewById(R.id.predictiveLV);

                        mResultAdapter = new PredictiveResultCursorAdapter(MainActivity.this, null, valueToFind);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        popupRelativeLayout = (RelativeLayout) findViewById(R.id.content_main);
        Context context = getApplicationContext();
        CharSequence text = "app compartida";
        int duration = Toast.LENGTH_SHORT;
        switch (item.getItemId()) {

            case R.id.action_select_language:
                // User chose the "Select  Language" action, mark the current item
                mResultList = (ListView) findViewById(R.id.predictiveLV);
                mResultList.setAdapter(null);
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


                String valueToFind = wordtf.getQuery().toString();

                swtoFind = valueToFind;
                mResultList.setAdapter(null);


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

                if(swtoFind != null && !swtoFind.isEmpty()){

                    mResultAdapter = new PredictiveResultCursorAdapter(MainActivity.this, null, valueToFind);

                    // Setup
                    mResultList.setAdapter(mResultAdapter);

                    // Instancia de helper
                    mTranslationDbHelper = new TranslationDbHelper(MainActivity.this);

                    // Carga de datos
                    loadPredictiveResults();

                    wordtf.setQuery(swtoFind,false);

                }else{
                    // Setup
                    //mResultList.setAdapter(null);
                    wordtf.setQuery("",false);

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

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.about_popup ,null);


                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setElevation(10);



                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }


                 Button acceptButton = (Button) customView.findViewById(R.id.popup_button_cancel);
                acceptButton.setText("Aceptar");

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mPopupWindow.dismiss();
                    }
                });


                mPopupWindow.showAtLocation(popupRelativeLayout, Gravity.CENTER,0,0);


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
