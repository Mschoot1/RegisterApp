package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marni.registerapp.Presentation.AsyncKlassen.CategoriesGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductAddTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductDeleteTask;
import com.example.marni.registerapp.Presentation.Domain.Allergy;
import com.example.marni.registerapp.Presentation.Domain.Category;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Presentation.Fragments.AllergiesFragment;
import com.example.marni.registerapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class AddProductActivity extends AppCompatActivity implements
        ProductAddTask.PostSuccessListener,
        CategoriesGetTask.OnCategoryAvailable,
        ProductDeleteTask.SuccessListener,
        AllergiesFragment.OnItemsSelected {

    private final String TAG = getClass().getSimpleName();

    private EditText etName;
    private EditText etPrice;
    private EditText etSize;
    private EditText etAlcohol;

    private Spinner sCategory;

    private LinearLayout linearLayout;

    private ImageView imageViewProduct;
    private static final int RESULT_LOAD_IMAGE = 1;
    private boolean imageChanged = false;

    private ImageView imageViewAddAllergy;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> categories = new ArrayList<>();

    private Product product = new Product();

    public static final String JWT_STR = "jwt_str";
    String jwt;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            imageViewProduct.setImageURI(selectedImage);
            imageChanged = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewProduct = (ImageView) findViewById(R.id.image);
        etName = (EditText) findViewById(R.id.name);
        etPrice = (EditText) findViewById(R.id.price);
        etSize = (EditText) findViewById(R.id.size);
        etAlcohol = (EditText) findViewById(R.id.alcohol_percentage);
        sCategory = (Spinner) findViewById(R.id.category_spinner);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        imageViewAddAllergy = (ImageView) findViewById(R.id.imageViewAddAllergy);

        getCategories("https://mysql-test-p4.herokuapp.com/product/categories");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapter);
        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(categories.get(position).equals("Non-alcoholic beverage")) {
                    etAlcohol.setText("0.0");
                    etAlcohol.setEnabled(false);
                } else {
                    etAlcohol.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        imageViewAddAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAllergyDialog();
            }
        });

        Button save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
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
                    Log.i(TAG, "strAllergies: " + strAllergies);

                    Bitmap image =((BitmapDrawable) imageViewProduct.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                    addProduct(
                            "https://mysql-test-p4.herokuapp.com/product/add",
                            strAllergies,
                            encodedImage,
                            etName.getText().toString(),
                            etPrice.getText().toString(),
                            etSize.getText().toString(),
                            etAlcohol.getText().toString(),
                            sCategory.getSelectedItem().toString());
                }
            }
        });
    }

    private void setAllergyIcons() {
        linearLayout.removeAllViews();
        ArrayList<Allergy> allergies = product.getAllergies();
        for (Allergy allergy : allergies) {
            linearLayout.addView(getImageView(allergy));
        }
        TextView textView = (TextView) findViewById(R.id.textViewNoAllergies);
        if (allergies.size() == 0) {
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

    private void getCategories(String apiUrl) {
        CategoriesGetTask categoriesGetTask = new CategoriesGetTask(this);
        String[] urls = new String[]{ apiUrl, jwt };
        categoriesGetTask.execute(urls);
    }

    private void addProduct(String apiUrl, String allergies, String img_url, String name, String price, String size, String alcohol, String categoryId) {
        String[] urls = new String[]{apiUrl, allergies, img_url, name, price, size, alcohol, categoryId, jwt};
        ProductAddTask task = new ProductAddTask(this);
        task.execute(urls);
    }

    private boolean allCellsAreFilled() {
        String ed_text = etName.getText().toString().trim();
        String ed2_text = etPrice.getText().toString().trim();
        String ed3_text = etSize.getText().toString().trim();
        String ed4_text = etAlcohol.getText().toString().trim();

        if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("")) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ed2_text.isEmpty() || ed2_text.length() == 0 || ed2_text.equals("")) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ed3_text.isEmpty() || ed3_text.length() == 0 || ed3_text.equals("")) {
            Toast.makeText(this, "Enter size", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ed4_text.isEmpty() || ed4_text.length() == 0 || ed4_text.equals("")) {
            Toast.makeText(this, "Enter alcohol percentage", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageChanged == false) {
            Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void postSuccessful(Boolean successful) {
        Toast.makeText(this, "Product successfully added", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), AssortmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCategoryAvailable(Category category) {
        Log.i(TAG, "category.getCategoryName(): " + category.getCategoryName());
        categories.add(category.getCategoryName());
        adapter.notifyDataSetChanged();
        if(product.getCategoryId() == category.getCategoryId()) {
            int i = 0;
            for(String categoryName : categories) {
                if (categoryName.equals(category.getCategoryName())) {
                    sCategory.setSelection(i);
                }
                i++;
            }
        }
    }

    private ImageView getImageView(Allergy allergy) {
        ImageView imageView = new ImageView(getApplicationContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageViewAddAllergy.getLayoutParams());
        lp.setMargins(5, 0, 0, 0);
        imageView.setLayoutParams(lp);

        int id = getResources().getIdentifier(allergy.getImage_url(), "mipmap", getPackageName());
        Log.i(TAG, "id: " + id);
        imageView.setImageResource(id);

        return imageView;
    }

    @Override
    public void successful(Boolean successful) {
        Toast.makeText(this, "Product successfully deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AssortmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(ArrayList<Allergy> allergies) {
        product.setAllergies(allergies);
        setAllergyIcons();
    }
}
