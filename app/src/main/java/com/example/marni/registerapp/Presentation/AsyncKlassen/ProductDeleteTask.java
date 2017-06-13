package com.example.marni.registerapp.Presentation.AsyncKlassen;

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

/**
 * Created by marcu on 6/7/2017.
 */

public class ProductDeleteTask extends AsyncTask<String, Void, Boolean> {
    private final String TAG = getClass().getSimpleName();
    private SuccessListener listener;

    public ProductDeleteTask(SuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String ConfirmUrl = params[0];

        Boolean response = null;

        Log.i(TAG, "doInBackground - " + ConfirmUrl);
        try {
            URL url = new URL(ConfirmUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("DELETE");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[2]);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("product_id", params[1]);

            Log.i(TAG, String.valueOf(jsonParam));

            DataOutputStream localDataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            localDataOutputStream.writeBytes(jsonParam.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();
            httpConnection.connect();

            responseCode = httpConnection.getResponseCode();
            response = (responseCode == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e(TAG, "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void onPostExecute(Boolean response) {
        listener.successful(response);
    }

    public interface SuccessListener {
        void successful(Boolean successful);
    }
}

