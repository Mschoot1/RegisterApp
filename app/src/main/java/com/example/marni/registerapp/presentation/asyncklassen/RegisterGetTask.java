package com.example.marni.registerapp.presentation.asyncklassen;

/**
 * Created by Wilco on 11-5-2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.presentation.domain.Register;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.marni.registerapp.presentation.asyncklassen.AccountGetTask.getStringFromInputStream;

public class RegisterGetTask extends AsyncTask<String, Void, String> {

    private final String tag = getClass().getSimpleName();

    public RegisterGetTask(OnRandomRegisterAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String orderUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(tag, "doInBackground - " + orderUrl);
        try {
            // Maak een URL object
            URL url = new URL(orderUrl);
            // Open een connection op de URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialiseer een HTTP connectie
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[1]);

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(tag, "doInBackground response = " + response);
            } else {
                Log.e(tag, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e(tag, "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }


    private OnRandomRegisterAvailable listener = null;

    @Override
    protected void onPostExecute(String response) {

        Log.i(tag, "onPostExecute " + response);

        // Check of er een response is
        if(response == null || response == "") {
            Log.e(tag, "onPostExecute kreeg een lege response!");
            return;
        }

        // Het resultaat is in ons geval een stuk tekst in JSON formaat.
        // Daar moeten we de info die we willen tonen uit filteren (parsen).
        // Dat kan met een JSONObject.
        JSONArray jsonArray;
        JSONObject jsonObject;

        try {

            jsonObject = new JSONObject(response);

            // Top level json object
            jsonArray = jsonObject.getJSONArray("results");

            for(int idx = 0; idx < jsonArray.length(); idx++) {
                JSONObject register = jsonArray.getJSONObject(idx);

                int id = register.getInt("id");
                String timestamp = register.getString("timestamp");
                double price_total = register.getDouble("price_total");
                int customer_id = register.getInt("customer_id");
                String timestamp2 = getFormattedDate(timestamp);

                Register o = new Register(id,timestamp2,price_total,customer_id);

                listener.onRandomRegisterAvailable(o);
            }

        } catch( JSONException ex) {
            Log.e(tag, "onPostExecute JSONException " + ex.getLocalizedMessage());
        } catch (ParseException e) {
            Log.e(tag, "onPostExecute ParseException " + e.getLocalizedMessage());
        }
    }

    private String getFormattedDate(String s) throws ParseException {

        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsedDate = sdf.parse(s);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = sdf.format(parsedDate);

        return formattedDate;
    }

    public interface OnRandomRegisterAvailable {
        void onRandomRegisterAvailable(Register register);
    }

}