package com.example.marni.registerapp.Presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.DummyGenerators.ProductGenerator;
import com.example.marni.registerapp.R;

public class OrderActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    public static class AddItemActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_item);
        }
    }
}
