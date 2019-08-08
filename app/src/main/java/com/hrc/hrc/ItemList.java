package com.hrc.hrc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ItemList extends AppCompatActivity {
    private String ProductName;
    private TextView productListname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        ProductName = getIntent().getStringExtra("Product");

        productListname = findViewById(R.id.item_name);

        productListname.setText(ProductName);
    }
}
