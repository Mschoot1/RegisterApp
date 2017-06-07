package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductDeleteTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductPutTask;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.R;
import com.squareup.picasso.Picasso;

public class AddItemActivity extends AppCompatActivity implements ProductPutTask.PutSuccessListener, ProductDeleteTask.SuccessListener {

    private EditText etName;
    private EditText etPrice;
    private EditText etSize;
    private EditText etAlcohol;
    private Spinner sCategory;
    private Button delete, save;

    private final String TAG = getClass().getSimpleName();

    ///
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final Product product;
        product = (Product) getIntent().getExtras().getSerializable("PRODUCT");

        final ImageView iv = (ImageView) findViewById(R.id.image);
        etName = (EditText) findViewById(R.id.name);
        etPrice = (EditText) findViewById(R.id.price);
        etSize = (EditText) findViewById(R.id.size);
        etAlcohol = (EditText) findViewById(R.id.alcohol_percentage);
//        sCategory = (Spinner) findViewById(R.id.category_spinner);

        assert product != null;
        Picasso.with(getApplicationContext()).load(product.getImagesrc()).into(iv);
        etName.setText(product.getName());
        etPrice.setText(String.valueOf(product.getPrice()));
        etSize.setText(String.valueOf(product.getSize()));
        etAlcohol.setText(String.valueOf(product.getAlcohol_percentage()));

        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                if (allCellsAreFilled()) {
                    putProduct(
                            "https://mysql-test-p4.herokuapp.com/product/edit",
                            "1,2",
                            product.getId() + "",
                            product.getImagesrc(),
                            etName.getText().toString(),
                            etPrice.getText().toString(),
                            etSize.getText().toString(),
                            etAlcohol.getText().toString(),
                            "1");
                }
            }
        });

        delete = (Button)findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, product.getId() + "");

                AlertDialog.Builder alert = new AlertDialog.Builder(AddItemActivity.this);
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

    private void putProduct(String apiUrl, String allergies, String productId, String img_url, String name, String price, String size, String alcohol, String categoryId) {
        String[] urls = new String[]{apiUrl, allergies, productId, img_url, name, price, size, alcohol, categoryId};
        ProductPutTask task = new ProductPutTask(this);
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
    public void putSuccessful(Boolean successful) {
        Toast.makeText(this, "Product successfully edited", Toast.LENGTH_LONG).show();
    }

    public void deleteProduct(String productid){
        ProductDeleteTask product = new ProductDeleteTask(this);
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/product/delete/", productid};
        product.execute(urls);
    }

    @Override
    public void successful(Boolean successful) {
        Toast.makeText(this, "Product succesfully deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AssortmentActivity.class);
        startActivity(intent);
    }
}