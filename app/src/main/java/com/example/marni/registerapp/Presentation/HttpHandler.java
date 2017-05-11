package com.example.marni.registerapp.Presentation;

/**
 * Created by Wilco on 11-5-2017.
 */

import android.os.AsyncTask;
import android.util.Log;

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

import static android.content.ContentValues.TAG;

public class HttpHandler extends AsyncTask<String, Void, String> {

    HttpURLConnection urlConnection;

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

    private OnRandomOrderAvailable listener = null;

    public HttpHandler(OnRandomOrderAvailable listener) {
        this.listener = listener;
    }

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
        try {
            // Top level json object
            jsonArray = new JSONArray(response);

            // Get all orderss and start looping
            for(int idx = 0; idx < jsonArray.length(); idx++) {
                JSONArray array = jsonArray.getJSONArray(idx);
                for(int j = 0; j < array.length(); j++) {
                    JSONObject jsonObject=array.getJSONObject(j);
                    int id = jsonObject.getInt("id");
                    String status = jsonObject.getString("status");
                    String timestamp = jsonObject.getString("timestamp");
                    String price_total = jsonObject.getString("price_total");
                    int customer_id = jsonObject.getInt("customer_id");
                    Log.i(TAG, id + " " + status + " " + timestamp + " " + price_total + " " + customer_id);

                    // Create new Order object
                    Order o = new Order();
                    o.setId(id);
                    o.setStatus(status);
                    o.setTimestamp(timestamp);
                    o.setPrice_total(price_total);
                    o.setCustomer_id(customer_id);

                    //
                    // call back with new order data
                    //
                    listener.onRandomOrderAvailable(o);
                }
                // array level objects and get orders






            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }

    public interface OnRandomOrderAvailable {
        void onRandomOrderAvailable(Order order);
    }

}
