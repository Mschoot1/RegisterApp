package com.example.marni.registerapp.presentation.asyncklassen;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ProductPutTask extends AsyncTask<String, Void, Boolean> {

    private final String tag = getClass().getSimpleName();

    private PutSuccessListener listener;

    public ProductPutTask(PutSuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String productPutUrl = params[0];

        Boolean response = null;

        Log.i(tag, "doInBackground - " + productPutUrl);
        try {
            URL url = new URL(productPutUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return false;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("PUT");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[9]);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("allergies", params[1]);
            jsonParam.put("product_id", params[2]);
            jsonParam.put("image", params[3]);
            jsonParam.put("name", params[4]);
            jsonParam.put("price", params[5]);
            jsonParam.put("size", params[6]);
            jsonParam.put("alcohol", params[7]);
            jsonParam.put("category_name", params[8]);

            Log.i(tag, String.valueOf(jsonParam));

            DataOutputStream localDataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            localDataOutputStream.writeBytes(jsonParam.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();
            httpConnection.connect();

            responseCode = httpConnection.getResponseCode();
            response = (responseCode == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return false;
        } catch (IOException e) {
            Log.e(tag, "doInBackground IOException " + e.getLocalizedMessage());
            return false;
        } catch (JSONException e) {
            Log.e(tag, "onPostExecute JSONException " + e.getLocalizedMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        listener.putSuccessful(response);
    }

    public interface PutSuccessListener {
        void putSuccessful(Boolean successful);
    }
}
