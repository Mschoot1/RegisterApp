package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.marni.registerapp.Presentation.Presentation.Activities.AssortmentActivity.PRODUCT;

public class AddProductActivity extends AppCompatActivity implements
        ProductAddTask.PostSuccessListener,
        CategoriesGetTask.OnCategoryAvailable,
        ProductDeleteTask.SuccessListener,
        AllergiesFragment.OnItemsSelected {

    private final String TAG = getClass().getSimpleName();

    private Product product;

    private EditText etName;
    private EditText etPrice;
    private EditText etSize;
    private EditText etAlcohol;

    private LinearLayout linearLayout;

    private ImageView imageViewAddAllergy;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> categories = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView iv = (ImageView) findViewById(R.id.image);
        etName = (EditText) findViewById(R.id.name);
        etPrice = (EditText) findViewById(R.id.price);
        etSize = (EditText) findViewById(R.id.size);
        etAlcohol = (EditText) findViewById(R.id.alcohol_percentage);
        final Spinner sCategory = (Spinner) findViewById(R.id.category_spinner);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        imageViewAddAllergy = (ImageView) findViewById(R.id.imageViewAddAllergy);

        getCategories("https://mysql-test-p4.herokuapp.com/product/categories");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapter);

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

                    Log.i(TAG, "strAllergies: " + strAllergies);
                    addProduct(
                            "https://mysql-test-p4.herokuapp.com/product/add",
                            strAllergies,
                            "",
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
        String[] urls = new String[]{ apiUrl };
        categoriesGetTask.execute(urls);
    }

    private void addProduct(String apiUrl, String allergies, String img_url, String name, String price, String size, String alcohol, String categoryId) {
        String[] urls = new String[]{apiUrl, allergies, img_url, name, price, size, alcohol, categoryId};
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
        return true;
    }

    @Override
    public void postSuccessful(Boolean successful) {
        Toast.makeText(this, "Product successfully added", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCategoryAvailable(Category category) {
        Log.i(TAG, "category.getCategoryName(): " + category.getCategoryName());
        categories.add(category.getCategoryName());
        adapter.notifyDataSetChanged();
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
        Toast.makeText(this, "Product succesfully deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AssortmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemsSelected(ArrayList<Allergy> allergies) {
        product.setAllergies(allergies);
        setAllergyIcons();
    }
}
