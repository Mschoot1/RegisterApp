package com.example.marni.registerapp.presentation.asyncklassen;

/**
 * Created by marcu on 6/8/2017.
 */

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

public class ProductAddTask extends AsyncTask<String, Void, Boolean> {

    private final String tag = getClass().getSimpleName();

    private PostSuccessListener listener;

    public ProductAddTask(PostSuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String balanceUrl = params[0];

        Boolean response = null;

        Log.i(tag, "doInBackground - " + balanceUrl);
        try {
            URL url = new URL(balanceUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return false;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Authorization", "Bearer " + params[8]);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("allergies", params[1]);
            jsonParam.put("image", params[2]);
            jsonParam.put("name", params[3]);
            jsonParam.put("price", params[4]);
            jsonParam.put("size", params[5]);
            jsonParam.put("alcohol", params[6]);
            jsonParam.put("category_name", params[7]);

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
        listener.postSuccessful(response);
    }

    public interface PostSuccessListener {
        void postSuccessful(Boolean successful);
    }
}
