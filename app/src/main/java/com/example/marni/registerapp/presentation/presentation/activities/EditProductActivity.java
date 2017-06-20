package com.example.marni.registerapp.presentation.presentation.activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marni.registerapp.presentation.asyncklassen.ProductDeleteTask;
import com.example.marni.registerapp.presentation.asyncklassen.ProductPutTask;
import com.example.marni.registerapp.presentation.domain.Allergy;
import com.example.marni.registerapp.presentation.domain.Product;
import com.example.marni.registerapp.presentation.presentation.fragments.AllergiesFragment;
import com.example.marni.registerapp.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.marni.registerapp.presentation.presentation.activities.AssortmentActivity.PRODUCT;

public class EditProductActivity extends AppCompatActivity implements
        ProductPutTask.PutSuccessListener,
        ProductDeleteTask.SuccessListener,
        AllergiesFragment.OnItemsSelected {

    private final String tag = getClass().getSimpleName();

    private Product product;
    private EditText etName;
    private EditText etPrice;
    private EditText etSize;
    private EditText etAlcohol;

    private LinearLayout linearLayout;

    private ImageView imageViewProduct;
    private static final int RESULT_LOAD_IMAGE = 1;
    private boolean imageChanged = false;
    String imageProduct;

    private ImageView imageViewAddAllergy;

    public static final String JWT_STR = "jwt_str";
    String jwt;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();

            imageViewProduct.setImageURI(selectedImage);
            imageChanged = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product = (Product) getIntent().getExtras().getSerializable(PRODUCT);

        imageViewProduct = (ImageView) findViewById(R.id.image);
        etName = (EditText) findViewById(R.id.name);
        etPrice = (EditText) findViewById(R.id.price);
        etSize = (EditText) findViewById(R.id.size);
        etAlcohol = (EditText) findViewById(R.id.alcoholpercentage);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        imageViewAddAllergy = (ImageView) findViewById(R.id.imageViewAddAllergy);

        assert product != null;
        if(!product.getImagesrc().equals("")){
            Picasso.with(getApplicationContext()).load(product.getImagesrc()).into(imageViewProduct);
        }

        etName.setText(product.getName());
        etPrice.setText(Double.toString(product.getPrice()));
        etSize.setText(Integer.toString(product.getSize()));
        etAlcohol.setText(Double.toString(product.getAlcoholpercentage()));

        if (Double.compare(product.getAlcoholpercentage(), 0.0) == 1) {
            etAlcohol.setEnabled(false);
        }

        setAllergyIcons();

        imageViewAddAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAllergyDialog();
            }
        });


        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (allCellsAreFilled()) {

                    String strAllergies = "";
                    int i = 1;
                    for (Allergy a : product.getAllergies()) {
                        strAllergies += a.getInformationText();
                        if (i < product.getAllergies().size()) {
                            strAllergies += ",";
                        }
                        i++;
                    }
                    Log.i(tag, "strAllergies: " + strAllergies);

                    Bitmap image =((BitmapDrawable) imageViewProduct.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                    if(imageChanged){
                        imageProduct = encodedImage;
                    } else {
                        imageProduct = product.getImagesrc();
                    }

                    putProduct(
                            "https://mysql-test-p4.herokuapp.com/product/edit",
                            strAllergies,
                            Integer.toString(product.getId()),
                            imageProduct,
                            etName.getText().toString(),
                            etPrice.getText().toString(),
                            etSize.getText().toString(),
                            etAlcohol.getText().toString(),
                            product.getCategoryName());
                }
            }
        });

        Button delete = (Button) findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, Integer.toString(product.getId()));

                AlertDialog.Builder alert = new AlertDialog.Builder(EditProductActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(Integer.toString(product.getId()));
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });
    }

    private void setAllergyIcons() {
        linearLayout.removeAllViews();
        List<Allergy> allergies = product.getAllergies();
        for (Allergy allergy : allergies) {
            linearLayout.addView(getImageView(allergy));
        }
        TextView textView = (TextView) findViewById(R.id.textViewNoAllergies);
        if (allergies.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }

    }

    private void showAddAllergyDialog() {
        FragmentManager fm = getFragmentManager();
        AllergiesFragment alertDialog = AllergiesFragment.newInstance(product.getAllergies());
        alertDialog.show(fm, "fragment_alert");
    }

    private void putProduct(String apiUrl, String allergies, String productId, String imgurl, String name, String price, String size, String alcohol, String categoryName) {
        String[] urls = new String[]{apiUrl, allergies, productId, imgurl, name, price, size, alcohol, categoryName, jwt};
        ProductPutTask task = new ProductPutTask(this);
        task.execute(urls);
    }

    private boolean allCellsAreFilled() {
        String edtext = etName.getText().toString().trim();
        String ed2text = etPrice.getText().toString().trim();
        String ed3text = etSize.getText().toString().trim();
        String ed4text = etAlcohol.getText().toString().trim();

        if (edtext.isEmpty() || edtext.length() == 0 || edtext.equals("")) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ed2text.isEmpty() || ed2text.length() == 0 || ed2text.equals("")) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ed3text.isEmpty() || ed3text.length() == 0 || ed3text.equals("")) {
            Toast.makeText(this, "Enter size", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ed4text.isEmpty() || ed4text.length() == 0 || ed4text.equals("")) {
            Toast.makeText(this, "Enter alcohol percentage", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void putSuccessful(Boolean successful) {
        Toast.makeText(this, "Product successfully edited", Toast.LENGTH_LONG).show();
    }

    private ImageView getImageView(Allergy allergy) {
        ImageView imageView = new ImageView(getApplicationContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageViewAddAllergy.getLayoutParams());
        lp.setMargins(5, 0, 0, 0);
        imageView.setLayoutParams(lp);

        int id = getResources().getIdentifier(allergy.getImageurl(), "mipmap", getPackageName());
        Log.i(tag, "id: " + id);
        imageView.setImageResource(id);

        return imageView;
    }

    public void deleteProduct(String productid){
        ProductDeleteTask productDeleteTask = new ProductDeleteTask(this);
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/product/delete/", productid, jwt};
        productDeleteTask.execute(urls);
    }

    @Override
    public void successful(Boolean successful) {
        Toast.makeText(this, "Product succesfully deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AssortmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(List<Allergy> allergies) {
        product.setAllergies(allergies);
        setAllergyIcons();
    }
}