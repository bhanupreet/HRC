package com.hrc.hrc;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private FloatingActionButton addBtn;
    private Toolbar productAppBar;
    private List<Item> ProductsList, ProductsearchList, allProductslist;
    private ProductsAdapter adapter;
    private RecyclerView productsRecycler;
    private String prodnamstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        FirebaseApp.initializeApp(this);

        addBtn = findViewById(R.id.productaddBtn);
        productAppBar = findViewById(R.id.productappbar);

        ProductsearchList = new ArrayList<>();
        allProductslist = new ArrayList<>();

        setSupportActionBar(productAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");

        productsRecycler = findViewById(R.id.productrecyler);
        productsRecycler.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        productsRecycler.setLayoutManager(layoutManager);

        ProductsList = new ArrayList<>();

        adapter = new ProductsAdapter(this, ProductsList);
        productsRecycler.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(ProductActivity.this, AddProductActivity.class);
                startActivity(addProductIntent);
            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Product");
        query.keepSynced(true);
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.search_actions, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);// Do not iconify the widget; expand it by default

        for (Item item : ProductsList) {
            allProductslist.add(item);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {

                if (newText.equals("")) {
                    ProductsList.clear();
                    for (Item item : allProductslist) {
                        ProductsList.add(item);
                    }
                    adapter.notifyDataSetChanged();

                    return true;
                } else {
                    ProductsearchList.clear();
                    for (Item item : allProductslist) {
                        if (!TextUtils.isEmpty(item.itemName)) {
                            if (item.itemName.toLowerCase().contains(newText)) {
                                ProductsearchList.add(item);
                            }
                        }
                    }
                    ProductsList.clear();
                    for (Item item : ProductsearchList) {
                        ProductsList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }

            public boolean onQueryTextSubmit(final String newText) {
                if (newText.equals("")) {
                    ProductsList.clear();
                    for (Item item : allProductslist) {
                        ProductsList.add(item);
                    }
                    adapter.notifyDataSetChanged();

                    return true;
                } else {
                    ProductsearchList.clear();
                    for (Item item : allProductslist) {
                        if (!TextUtils.isEmpty(item.itemName)) {
                            if (item.itemName.toLowerCase().contains(newText)) {
                                ProductsearchList.add(item);
                            }
                        }
                    }
                    ProductsList.clear();
                    for (Item item : ProductsearchList) {
                        ProductsList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    return true;
                }

            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ProductsList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    ProductsList.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

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
//            productsViewholder.itemOneDesc.setText(Products.itemOneDesc);
            productsViewholder.itemname.setText(Products.itemName);
            productsViewholder.itemOneDesc.setTextSize(0F);
            Picasso.get().load(Products.image).into(productsViewholder.itemImage);
            prodnamstring = ProductsList.get(i).itemName;

            productsViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ItemIntent = new Intent(ProductActivity.this, ItemListActivity.class);
                    ItemIntent.putExtra("Product", ProductsList.get(i).itemName);
                    startActivity(ItemIntent);
                }
            });

            productsViewholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    {
                        AlertDialog.Builder delete = new AlertDialog.Builder(ProductActivity.this);
                        CharSequence[] options = new CharSequence[]{"Edit"};
                        delete.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Query q1 = FirebaseDatabase.getInstance().getReference().child("Product").orderByChild("itemName").equalTo(prodnamstring);
                                    q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                            String itemrefstring = nodeDataSnapshot.getKey();
                                            Intent prodIntent = new Intent(ProductActivity.this, AddProductActivity.class);
                                            prodIntent.putExtra("product_name", prodnamstring);
                                            prodIntent.putExtra("prod_ref", itemrefstring);
                                            prodIntent.putExtra("image", Products.image);
                                            startActivity(prodIntent);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        });
                        delete.show();
                        return true;
                    }
                }
            });
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
