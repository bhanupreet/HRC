package com.hrc.hrc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private FloatingActionButton addBtn;
    private Toolbar productAppBar;
    private List<Item> ProductsList;
    private ProductsAdapter adapter;
    private RecyclerView productsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        FirebaseApp.initializeApp(this);

        addBtn = findViewById(R.id.productaddBtn);
        productAppBar = findViewById(R.id.productappbar);


        setSupportActionBar(productAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");

        productsRecycler = findViewById(R.id.productrecyler);
        productsRecycler.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        productsRecycler.setLayoutManager(layoutManager);

        ProductsList = new ArrayList<>();
        Item item1 = new Item("name1", "desc1", "full desc");

        adapter = new ProductsAdapter(this, ProductsList);
        productsRecycler.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(ProductActivity.this, AddProductActivity.class);
                startActivity(addProductIntent);
            }
        });

    }

    private class ProductsAdapter extends RecyclerView.Adapter<ProductsViewholder> {
        private Context mctx;
        private List<Item> itemList;


        public ProductsAdapter(Context mctx, List<Item> itemList) {
            this.mctx = mctx;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ProductsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(mctx).inflate(R.layout.single_item_layout, parent, false);
            return new ProductsViewholder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull final ProductsViewholder productsViewholder, final int i) {
            final Item Products = ProductsList.get(i);
            productsViewholder.itemOneDesc.setText(Products.itemOneDesc);
            productsViewholder.itemname.setText(Products.itemName);

        }

        @Override
        public int getItemCount() {
            return ProductsList.size();
        }

    }

    private class ProductsViewholder extends RecyclerView.ViewHolder {

        TextView itemname, itemOneDesc;
        ImageView itemImage;

        public ProductsViewholder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.itemname);
            itemOneDesc = itemView.findViewById(R.id.onelinedesc);
            itemImage = itemView.findViewById(R.id.productimage);
        }
    }
}