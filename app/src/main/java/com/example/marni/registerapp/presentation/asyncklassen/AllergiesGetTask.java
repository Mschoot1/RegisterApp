package com.example.marni.registerapp.presentation.asyncklassen;

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.presentation.domain.Allergy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.example.marni.registerapp.presentation.asyncklassen.AccountGetTask.getStringFromInputStream;

public class AllergiesGetTask extends AsyncTask<String, Void, String> {

    private final String tag = getClass().getSimpleName();

    private OnAllergyAvailable listener = null;

    public AllergiesGetTask(OnAllergyAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String personUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(tag, "doInBackground - " + personUrl);
        try {
            // Maak een URL object
            URL url = new URL(personUrl);
            // Open een connection op de URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialiseer een HTTP connectie
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[1]);

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                Log.i(tag, "doInBackground response = " + response);
            } else {
                Log.e(tag, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("tag", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.

        return response;
    }


    @Override
    protected void onPostExecute(String response) {
        JSONArray jsonArray;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            jsonArray = jsonObject.getJSONArray("results");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject allergy = jsonArray.getJSONObject(i);

                String allergieimage = allergy.getString("image");
                String allergieinformatie = allergy.getString("description");

                Allergy a = new Allergy(allergieimage, allergieinformatie);

                listener.onAllergyAvailable(a);
            }
        } catch (JSONException e) {
            Log.e(tag, "onPostExecute JSONException " + e.getLocalizedMessage());
        }
    }

    public interface OnAllergyAvailable {
        void onAllergyAvailable(Allergy allergy);
    }
}

