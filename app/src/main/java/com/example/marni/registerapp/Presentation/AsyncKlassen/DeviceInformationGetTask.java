package com.example.marni.registerapp.Presentation.AsyncKlassen;

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.Presentation.Domain.Deviceinformation;

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

import static com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask.getStringFromInputStream;

/**
 * Created by Wallaard on 16-5-2017.
 */

public class DeviceInformationGetTask extends AsyncTask<String, Void, String> {
    private final String tag = getClass().getSimpleName();
    private OnDeviceInformationAvailable listener = null;

    public DeviceInformationGetTask(OnDeviceInformationAvailable listener) {
        this.listener = listener;
    }

    public interface OnDeviceInformationAvailable{
        void OnDeviceInformationAvailable(Deviceinformation deviceinformation);
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
            Log.e("tag", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response){
        JSONObject jsonObject;
        JSONArray jsonArray;
        try{
            jsonObject = new JSONObject(response);
            jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length();i++){
                JSONObject deviceinfo = jsonArray.getJSONObject(i);

                String hardware = deviceinfo.getString("hardware");
                String type = deviceinfo.getString("type");
                String model = deviceinfo.getString("model");
                String brand = deviceinfo.getString("brand");
                String device = deviceinfo.getString("device");
                String manufacturer = deviceinfo.getString("manufacturer");
                String user = deviceinfo.getString("user");
                String serial = deviceinfo.getString("serial");
                String host = deviceinfo.getString("host");
                String id = deviceinfo.getString("id");
                String bootloader = deviceinfo.getString("bootloader");
                String board = deviceinfo.getString("board");
                String display = deviceinfo.getString("display");


                Deviceinformation d = new Deviceinformation(hardware,type,model,brand,device,manufacturer,user,serial,host,id,bootloader,board,display);

                listener.OnDeviceInformationAvailable(d);

            }
        } catch (JSONException e) {
            Log.e(tag, "onPostExecute JSONException " + e.getLocalizedMessage());
        }
    }
}
