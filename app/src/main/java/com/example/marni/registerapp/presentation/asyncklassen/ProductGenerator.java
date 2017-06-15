package com.example.marni.registerapp.presentation.asyncklassen;

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.presentation.domain.Product;

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

/**
 * Created by Wallaard on 9-5-2017.
 */

public class ProductGenerator extends AsyncTask<String,Void,String>{
    private final String tag = getClass().getSimpleName();
    private OnAvailable listener = null;
    public ProductGenerator(OnAvailable listener){
        this.listener = listener;
    }

    public interface OnAvailable{
        void OnAvailable(Product product);
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String personUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(tag, "doInBackground - " + personUrl);
        try {
            // Maak een URL object
            URL url = new URL(personUrl);
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

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.

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
                JSONObject product = jsonArray.getJSONObject(i);

                String productname = product.getString("name");
                double price = product.getDouble("price");
                int size = product.getInt("size");
                int quantity = product.getInt("quantity");
                int alcohol = product.getInt("alcohol");
                String categoryName = product.getString("category_name");
                int categoryId = product.getInt("category_id");
                String imagesrc = product.getString("product_image");

                Product p = new Product();
                p.setName(productname);
                p.setPrice(price);
                p.setSize(size);
                p.setQuantity(quantity);
                p.setAlcohol_percentage(alcohol);
                p.setCategoryName(categoryName);
                p.setCategoryId(categoryId);
                p.setImagesrc(imagesrc);

                //,string2,string3,string4

                listener.OnAvailable(p);

            }
        } catch (JSONException e) {
            Log.e(tag, "onPostExecute JSONException " + e.getLocalizedMessage());
        }
    }
}
