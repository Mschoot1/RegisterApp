package com.example.marni.registerapp.Presentation.AsyncKlassen;

/**
 * Created by Wallaard on 16-5-2017.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.example.marni.registerapp.Presentation.Presentation.Activities.LogInActivity;

import static android.content.ContentValues.TAG;
import static com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask.getStringFromInputStream;

/**
 * Created by marni on 4-5-2017.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class LoginTask extends AsyncTask<String, Void, String> {

    private SuccessListener listener;
    private final String tag = getClass().getSimpleName();

    private ProgressDialog dialog;

    public static final String UNAUTHORIZED = "Unauthorized";

    public LoginTask(LogInActivity activity) {

        this.listener = activity;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Authenticating. Please wait..");
        dialog.show();
    }


    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responseCode;
        String balanceUrl = params[0];

        String response = null;

        Log.i(tag, "doInBackground - " + balanceUrl);
        try {
            URL url = new URL(balanceUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("POST");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", params[1]);
            jsonParam.put("password", params[2]);

            Log.i(tag, String.valueOf(jsonParam));

            DataOutputStream localDataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            localDataOutputStream.writeBytes(jsonParam.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();
            httpConnection.connect();

            responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream).replace("\"", "");
            } else {
                response = UNAUTHORIZED;
            }        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e(tag, "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        } catch (JSONException e) {
            Log.e(tag, "onPostExecute JSONException " + e.getLocalizedMessage());
        }

        return response;
    }

    protected void onPostExecute(String response) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Log.i(tag, "response: " + response);

        listener.successful(response);
    }

    public interface SuccessListener {
        void successful(String response);
    }
}

