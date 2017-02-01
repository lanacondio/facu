package com.lanacondio.diccionarioguarani;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.FavoriteDbHelper;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.GdiccApiClient;

import org.json.JSONException;

import java.util.List;

import static android.R.attr.duration;


public class AllWordsWebActivity extends AppCompatActivity {

    private RelativeLayout popupRelativeLayout;
    private PopupWindow addPopupWindow;
    private View customView;
    private TranslationDbHelper mTranslationDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(new Preferences(this).getFontStyle().getResId(), true);
        this.setContentView(R.layout.activity_all_words_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_all_words_web);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton sharingb = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        FloatingActionButton copyText  = (FloatingActionButton)findViewById(R.id.copyToClipboardActionButton);

        FloatingActionButton addFavorite  = (FloatingActionButton)findViewById(R.id.addFavorite);

        FloatingActionButton addWord = (FloatingActionButton)findViewById(R.id.add_word_button);

        FavoriteDbHelper mFavoriteDbHelper = new FavoriteDbHelper(AllWordsWebActivity.this) ;
        String wtoFind = getIntent().getStringExtra("wordtf");

        if(mFavoriteDbHelper.isFavorite(wtoFind))
        {
            addFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_favorite_black_24dp));
        }


        sharingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                Context context = getApplicationContext();
                String wtoFind = getIntent().getStringExtra("wordtf");
                int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                TranslationDbHelper mTranslationDbHelper = new TranslationDbHelper(AllWordsWebActivity.this) ;
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

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                String wtoFind = getIntent().getStringExtra("wordtf");
                int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                TranslationDbHelper mTranslationDbHelper = new TranslationDbHelper(AllWordsWebActivity.this) ;
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
                FavoriteDbHelper mFavoriteDbHelper = new FavoriteDbHelper(AllWordsWebActivity.this) ;
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

        addWord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                popupRelativeLayout = (RelativeLayout) findViewById(R.id.content_all_words_web);
                // Inflate the custom layout/view
                customView = inflater.inflate(R.layout.add_word_popup ,null);


                // Initialize a new instance of popup window
                addPopupWindow = new PopupWindow(
                        customView,
                        AppBarLayout.LayoutParams.WRAP_CONTENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT
                );
                addPopupWindow.setOutsideTouchable(true);
                addPopupWindow.setFocusable(true);
                addPopupWindow.setElevation(10);

                if(Build.VERSION.SDK_INT>=21){
                    addPopupWindow.setElevation(5.0f);
                }

                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                Button acceptButton = (Button) customView.findViewById(R.id.popup_button_accept);
                acceptButton.setText("Aceptar");

                String wtoFind = getIntent().getStringExtra("wordtf");
                EditText nword = (EditText) customView.findViewById(R.id.et_new_word);
                nword.setText(wtoFind);


                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        addPopupWindow.dismiss();
                    }
                });

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText nword = (EditText) customView.findViewById(R.id.et_new_word);
                        EditText ntrad = (EditText) customView.findViewById(R.id.et_new_traduction);
                        EditText ncontext = (EditText) customView.findViewById(R.id.et_new_context);
                        EditText ntype = (EditText) customView.findViewById(R.id.et_new_type);

                        Translation translation = new Translation();
                        translation.setWordToFind(nword.getText().toString().toLowerCase().trim());
                        translation.setTranslationResult(ntrad.getText().toString());
                        translation.setContext(ncontext.getText().toString());
                        translation.setType(ntype.getText().toString());
                        int originalLanguaje = getIntent().getIntExtra("olanguage",1);
                        translation.setCountryId(originalLanguaje);
                        translation.setDeviceId("my device");

                        mTranslationDbHelper = new TranslationDbHelper(getApplicationContext());
                        ResultPostTask task = new ResultPostTask(translation);
                        task.execute();
                        addPopupWindow.dismiss();
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence text = getApplicationContext().getString(R.string.add_word_text);
                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();
                        AllWordsWebActivity.super.onBackPressed();
                    }
                });

                addPopupWindow.showAtLocation(popupRelativeLayout, Gravity.CENTER,0,0);

            }
        });

        AllWordsWebFragment fragment = (AllWordsWebFragment)
        getSupportFragmentManager().findFragmentById(R.id.content_all_words_web);

        if (fragment == null) {
            fragment = AllWordsWebFragment.newInstance();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_all_words_web, fragment)
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_words_web,menu);

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
                AllWordsWebActivity.super.onBackPressed();

                return true;

            case  R.id.favorites:

                Intent favorites = new Intent(AllWordsWebActivity.this, FavoritesActivity.class);
                startActivity(favorites);

                return true;
            case  R.id.Share_app:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String shareBody =  "";

                shareBody += context.getString(R.string.share_app_messages) +getApplicationContext().getPackageName();

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
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

    class ResultPostTask extends AsyncTask<Void,Void,Void> {

        Translation ttpost;
        public ResultPostTask(Translation translation){
            ttpost = translation;
        }
        @Override
        protected Void doInBackground(Void... params) {
            GdiccApiClient client = new GdiccApiClient();
            try {
                client.postWords(getApplicationContext(),  ttpost);
                int webId = client.getWebId();
                ttpost.setWebId(webId);

                mTranslationDbHelper.addTranslation(ttpost);
                // Carga de datos

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


    }




}
