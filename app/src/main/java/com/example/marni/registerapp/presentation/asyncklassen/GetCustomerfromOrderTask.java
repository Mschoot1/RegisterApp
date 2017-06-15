package com.example.marni.registerapp.presentation.asyncklassen;

/**
 * Created by MSI-PC on 30-5-2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.presentation.domain.Order;

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

public class GetCustomerfromOrderTask extends AsyncTask<String, Void, String> {

    private final String tag = getClass().getSimpleName();

    private OnCustomerIdAvailable listener = null;

    public GetCustomerfromOrderTask(OnCustomerIdAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        String personUrl = params[0];
        String response = "";

        Log.i(tag, "doInBackground - " + personUrl);
        try {
            URL url = new URL(personUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[1]);

            httpConnection.connect();

            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
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

        return response;
    }

    @Override
    protected void onPostExecute(String response) {

        Log.i(tag, "onPostExecute " + response);

        if (response == null || response == "") {
            Log.e(tag, "onPostExecute kreeg een lege response!");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            Log.i(tag, "results.length(): " + jsonArray.length());

            for (int idx = 0; idx < jsonArray.length(); idx++) {
                JSONObject order = jsonArray.getJSONObject(idx);

                int id = order.getInt("id");
                int customerId = order.getInt("customer_id");

                Order o = new Order();
                o.setOrderId(id);
                o.setCustomerid(customerId);

                listener.onCustomerIdAvailable(o);
            }
        } catch (JSONException ex) {
            Log.e(tag, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }

    public interface OnCustomerIdAvailable {
        void onCustomerIdAvailable(Order order);
    }
}
