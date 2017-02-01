package com.lanacondio.diccionarioguarani.service;

import android.content.Context;
import android.preference.PreferenceActivity;

import com.google.gson.Gson;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.*;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lanacondio on 30/1/2017.
 */

public class GdiccApiClient {
    private List<Translation> translations;
    private int webId;

    public List<Translation> getTranslations(){return this.translations;}

    public int getWebId(){return this.webId;}

    public void getWords(String word) throws JSONException {
        this.translations = new ArrayList<Translation>();
        HttpUtils.get("/translation?filter=word,eq,"+word, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {

                    JSONObject jsonObject = (JSONObject)response.get("translation"); // root JsonObject. i.e. "locations"

                    JSONArray jsonArray = (JSONArray)jsonObject.get("records");
                    if (jsonArray != null) {
                        int len = jsonArray.length();
                        for (int i=0;i<len;i++){
                            String[] properties = jsonArray.get(i).toString().replace("[","").replace("]","").replace('"', ' ').split(",");
                            Translation aux = new Translation();
                            aux.setId(Integer.parseInt(properties[0]));
                            aux.setWebId(Integer.parseInt(properties[0]));
                            aux.setCountryId(Integer.parseInt(properties[1]));
                            aux.setWordToFind(properties[2]);
                            aux.setTranslationResult(properties[3]);
                            aux.setContext(properties[4]);
                            aux.setType(properties[5]);
                            aux.setDeviceId(properties[6]);
                            translations.add(aux);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray translations) {
                // Pull out the first event on the public timeline
                JSONObject firstEvent = null;
                try {
                    firstEvent = translations.getJSONObject(0);

                    String translation = firstEvent.getString("translation");
                    System.out.println(translation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Do something with the response
            }
        });
    }


    public void postWords(Context context, Translation translation) throws JSONException {

        RequestParams rp = new RequestParams();
        rp.add("word", translation.getWordToFind());
        rp.add("translation", translation.getTranslationResult());
        rp.add("context", translation.getContext());
        rp.add("type", translation.getType());
        rp.add("origin_device_id", translation.getDeviceId());
        rp.add("language_id", translation.getCountryId().toString());

        HttpUtils.post(context, "/translation", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    webId = Integer.parseInt(response.toString()); // root JsonObject. i.e. "locations"
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
                try {
                    webId = Integer.parseInt(response.getString(0)); // root JsonObject. i.e. "locations"
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            public void onFailure(int statusCode, Header[] headers, String response, Throwable exception){

                String ex = response;
            }

        });

    }
}
