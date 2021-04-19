package com.lanacondio.diccionarioguarani.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.AllWordsWebActivity;
import com.lanacondio.diccionarioguarani.R;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Evaluation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationContract;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanacondio on 20/10/2016.
 */

public class WebResultCursorAdapter extends CursorAdapter {


    private Map<String, Translation> translationsList = new HashMap<String, Translation>();
    private TranslationDbHelper mTranslationDbHelper;
    private GdiccApiClient client;
    private Evaluation evaluation;

    public WebResultCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_word_web, viewGroup, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        //resultados

        mTranslationDbHelper = new TranslationDbHelper(context);
        client = new GdiccApiClient();
        final int position = cursor.getPosition()+1;

        TextView tIndex= (TextView) view.findViewById(R.id.tvIndex);
        //TextView ttype= (TextView) view.findViewById(R.id.tvType);
        final TextView ttranslation = (TextView) view.findViewById(R.id.tvTranslation);
        TextView tcontext = (TextView) view.findViewById(R.id.tvContext);
        FloatingActionButton downloadButton = (FloatingActionButton) view.findViewById(R.id.download_word);
        final FloatingActionButton thumbUpButton = (FloatingActionButton) view.findViewById(R.id.thumb_up);
        final FloatingActionButton thumbDownButton = (FloatingActionButton) view.findViewById(R.id.thumb_down);
        final TextView tevaluations = (TextView) view.findViewById(R.id.eval);
        final TextView tnegevaluations = (TextView) view.findViewById(R.id.negeval);
        //FloatingActionButton deleteButton = (FloatingActionButton) view.findViewById(R.id.delete_word);

        String strres = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TRANSLATION));
        String contexts = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.CONTEXT));
        String type = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.TYPE));
        String gcmid = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.GCM_ID));
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String wordtf = cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.WORD));
        int webid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.WEB_ID)));
        int languageId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TranslationContract.TranslationEntry.LANGUAGE_ID)));
        final int evaluations = Integer.parseInt(cursor.getString(cursor.getColumnIndex("evaluations")));
        final int negEvaluations = Integer.parseInt(cursor.getString(cursor.getColumnIndex("negevaluations")));
        boolean canEvaluate = Boolean.valueOf(cursor.getString(cursor.getColumnIndex("canEvaluate")));


        Translation ttadd = new Translation();

        ttadd.setWebId(webid);
        ttadd.setCountryId(languageId);
        ttadd.setWordToFind(wordtf);
        ttadd.setTranslationResult(strres);
        ttadd.setContext(contexts);
        ttadd.setType(type);
        ttadd.setDeviceId(gcmid);
        translationsList.put(strres, ttadd);

        ttranslation.setText(strres);
        //ttype.setText(type);
        tcontext.setText(contexts);

        if(evaluations>0)
        {
            tevaluations.setText("+"+String.valueOf(evaluations));
        }
        else{
            tevaluations.setText(String.valueOf(evaluations));
        }

        if(negEvaluations>0){
            tnegevaluations.setText("-"+String.valueOf(negEvaluations));
        }else{
            tnegevaluations.setText(String.valueOf(negEvaluations));
        }

        if(!strres.contains("No se encontraron resultados"))
        {
            tIndex.setText(String.valueOf(position)+".");

        }
        else
        {
            /*
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) downloadButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            downloadButton.setLayoutParams(p);*/
            downloadButton.setVisibility(View.GONE);

            /*
            p = (CoordinatorLayout.LayoutParams) thumbUpButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            thumbUpButton.setLayoutParams(p);*/
            thumbUpButton.setVisibility(View.GONE);

            /*
            p = (CoordinatorLayout.LayoutParams) thumbDownButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            thumbDownButton.setLayoutParams(p);*/
            thumbDownButton.setVisibility(View.GONE);

/*
            p = (CoordinatorLayout.LayoutParams) tevaluations.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            tevaluations.setLayoutParams(p);*/
            tevaluations.setVisibility(View.GONE);

            /*
            p = (CoordinatorLayout.LayoutParams) tnegevaluations.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            tnegevaluations.setLayoutParams(p);*/
            tnegevaluations.setVisibility(View.GONE);
            tIndex.setText("");
        }

        if(!gcmid.equals(deviceId))
        {
           // deleteButton.setVisibility(View.GONE);
        }
        else
        {

        }


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window

                Translation ttpost = translationsList.get(ttranslation.getText().toString());
                mTranslationDbHelper.addTranslation(ttpost);
                int duration = Toast.LENGTH_SHORT;
                CharSequence text = context.getString(R.string.donwload_word_text);
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }
        });



        if(canEvaluate) {
            thumbDownButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window

                    Translation ttpost = translationsList.get(ttranslation.getText().toString());
                    Evaluation evaluationAux = new Evaluation();
                    evaluationAux.setIdTranslation(ttpost.getWebId());
                    evaluationAux.setDeviceId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                    evaluationAux.setPoints(-1);
                    ResultPostTask task = new ResultPostTask(evaluationAux, context);
                    task.execute();

                    int neval = Integer.parseInt(tnegevaluations.getText().toString().replace("+", "")) - 1;

                    if (neval > 0) {
                        tnegevaluations.setText("-" + String.valueOf(neval));
                    } else {
                        tnegevaluations.setText(String.valueOf(neval));
                    }


                    thumbUpButton.setVisibility(View.GONE);

                    thumbDownButton.setVisibility(View.GONE);
                }
            });

            thumbUpButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window

                    Translation ttpost = translationsList.get(ttranslation.getText().toString());
                    Evaluation evaluationPositiveAux = new Evaluation();
                    evaluationPositiveAux.setIdTranslation(ttpost.getWebId());
                    evaluationPositiveAux.setDeviceId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                    evaluationPositiveAux.setPoints(1);

                    ResultPostTask task = new ResultPostTask(evaluationPositiveAux, context);
                    task.execute();

                    int npeval = Integer.parseInt(tevaluations.getText().toString().replace("+", "")) + 1;

                    if (npeval > 0) {
                        tevaluations.setText("+" + String.valueOf(npeval));
                    } else {
                        tevaluations.setText(String.valueOf(npeval));
                    }

                    thumbUpButton.setVisibility(View.GONE);

                    thumbDownButton.setVisibility(View.GONE);
                }
            });
        }else{
            thumbDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = context.getString(R.string.evaluation_done_text);
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            });

            thumbUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = context.getString(R.string.evaluation_done_text);
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            });
        }
    }

    private class ResultPostTask extends AsyncTask<Void, Void, Cursor> {
        Evaluation etpost;
        Context context;

        public ResultPostTask(Evaluation evaluation, Context cont){
            etpost = evaluation;
            context = cont;
        }

        @Override

        protected Cursor doInBackground(Void... voids) {
            /*
            try {
                client.postEvaluation(context, this.etpost);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = context.getString(R.string.evaluation_thanks_text);
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }
}
