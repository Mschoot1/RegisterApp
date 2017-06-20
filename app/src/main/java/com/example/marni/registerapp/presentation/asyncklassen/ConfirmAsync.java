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

/**
 * Created by Wallaard on 11-5-2017.
 */

public class ConfirmAsync extends AsyncTask<String, Void, Boolean> {
    private final String tag = getClass().getSimpleName();
    private SuccessListener listener;

    public ConfirmAsync(SuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String confirmUrl = params[0];

        Boolean response = null;

        Log.i(tag, "doInBackground - " + confirmUrl);
        try {
            URL url = new URL(confirmUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return false;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("PUT");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[3]);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("status", params[1]);
            jsonParam.put("id",params[2]);

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
        listener.successful(response);
    }

    public interface SuccessListener {
        void successful(Boolean successful);
    }
}
