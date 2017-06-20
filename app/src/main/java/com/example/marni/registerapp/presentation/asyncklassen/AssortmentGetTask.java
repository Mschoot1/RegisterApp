package com.example.marni.registerapp.presentation.asyncklassen;

import android.os.AsyncTask;
import android.util.Log;

import com.example.marni.registerapp.presentation.domain.Allergy;
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
import java.util.ArrayList;

import static com.example.marni.registerapp.presentation.asyncklassen.AccountGetTask.getStringFromInputStream;


public class AssortmentGetTask extends AsyncTask<String, Void, String> {
    private final String tag = getClass().getSimpleName();
    private OnProductAvailable listener = null;

    public AssortmentGetTask(OnProductAvailable listener) {
        this.listener = listener;
    }

    public interface OnProductAvailable{
        void onProductAvailable(Product product);
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
                Log.i(tag, "doInBackground response = " + response);
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
                JSONObject product = jsonArray.getJSONObject(i);

                JSONArray allergies = product.getJSONArray("allergies");

                Log.i(tag, "allergies.length(): " + allergies.length());

                ArrayList<Allergy> as = new ArrayList<>();
                for (int j = 0; j < allergies.length(); j++) {

                    JSONObject allergy = allergies.getJSONObject(j);
                    Allergy a = new Allergy(allergy.getString("image"), allergy.getString("description"));
                    as.add(a);
                }

                String name = product.getString("name");
                int id = product.getInt("id");
                int size = product.getInt("size");
                double price = product.getDouble("price");
                int alcohol = product.getInt("alcohol");
                String categoryName = product.getString("category_name");
                int categoryId = product.getInt("category_id");
                String imagesrc = product.getString("product_image");

                Log.i(tag, "name: " + name);

                Product p = new Product();
                p.setName(name);
                p.setCategoryName(categoryName);
                p.setSize(size);
                p.setAlcoholpercentage(alcohol);
                p.setPrice(price);
                p.setId(id);
                p.setCategoryId(categoryId);
                p.setImagesrc(imagesrc);
                p.setAllergies(as);

                listener.onProductAvailable(p);

            }
        } catch (JSONException e) {
            Log.e(tag, "onPostExecute JSONException " + e.getLocalizedMessage());
        }
    }
}


