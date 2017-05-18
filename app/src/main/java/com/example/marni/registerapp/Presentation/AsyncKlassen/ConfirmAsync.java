package com.example.marni.registerapp.Presentation.AsyncKlassen;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.marni.registerapp.Presentation.Domain.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Wallaard on 11-5-2017.
 */

public class ConfirmAsync extends AsyncTask<String, Void, Boolean> {
    private final String TAG = getClass().getSimpleName();
    private SuccessListener listener;

    public ConfirmAsync(SuccessListener listener) {
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

            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("PUT");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("status", params[1]);
            jsonParam.put("id",params[2]);

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
