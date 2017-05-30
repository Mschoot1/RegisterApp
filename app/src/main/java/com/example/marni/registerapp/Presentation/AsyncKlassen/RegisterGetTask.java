package com.example.marni.registerapp.Presentation.AsyncKlassen;

/**
 * Created by Wilco on 11-5-2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.Presentation.Domain.Register;

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

public class RegisterGetTask extends AsyncTask<String, Void, String> {

    private final String TAG = getClass().getSimpleName();

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

        Log.i(TAG, "doInBackground - " + orderUrl);
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

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(TAG, "doInBackground response = " + response);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    private OnRandomRegisterAvailable listener = null;

    @Override
    protected void onPostExecute(String response) {

        Log.i(TAG, "onPostExecute " + response);

        // Check of er een response is
        if(response == null || response == "") {
            Log.e(TAG, "onPostExecute kreeg een lege response!");
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
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        } catch (ParseException e) {
            e.printStackTrace();
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