package com.lanacondio.diccionarioguarani;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.PredictiveResultCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private Button allWords;
    private TextView languajeText;
    private TextView tlanguajeText;
    private Switch lselector;
    private ListView mResultList;
    private PredictiveResultCursorAdapter mResultAdapter;
    private TranslationDbHelper mTranslationDbHelper;
    private String swtoFind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText wordtf = (EditText) findViewById(R.id.textToFind);

        /*
        allWords = (Button)findViewById(R.id.findResultButton);
        allWords.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //EditText wordtf = (EditText) findViewById(R.id.textToFind);

                String valueToFind = wordtf.getText().toString();
                if(TextUtils.isEmpty(valueToFind)) {
                    wordtf.setError("Debe ingresar un valor");
                    return;
                }
                else{
                    Intent allWords = new Intent(MainActivity.this, AllWordsActivity.class);
                    allWords.putExtra("wordtf",valueToFind);
                    startActivity(allWords);
                }

            }

        });*/



        wordtf.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                String valueToFind = wordtf.getText().toString();
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
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        ImageButton lselector=(ImageButton) findViewById(R.id.chi_button);
        lselector.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //EditText wordtf = (EditText) findViewById(R.id.textToFind);

                languajeText = (TextView)findViewById(R.id.originLanguageTextView);
                tlanguajeText = (TextView)findViewById(R.id.translationLanguageTextView);
                if(languajeText.getText().toString() == getResources().getString(R.string.Guarani)){
                    languajeText.setText(getResources().getString(R.string.Spanish));
                    tlanguajeText.setText(getResources().getString(R.string.Guarani));
                }else{
                    languajeText.setText(getResources().getString(R.string.Guarani));
                    tlanguajeText.setText(getResources().getString(R.string.Spanish));
                }

            }

        });

    }

    private void loadPredictiveResults() {
        new ResultLoadTask().execute();
    }


    private class ResultLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            if(swtoFind != null && !swtoFind.isEmpty()) {
                return mTranslationDbHelper.getPredictiveTranslations(swtoFind);
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
