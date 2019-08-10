package com.hrc.hrc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {
    private String mProductName;
    private RecyclerView mItemListRecycler;
    private FloatingActionButton mItemaddBtn;
    private Toolbar mItemListAppBar;
    private List<Item> mItemList, allItemslist, searchItemsList;
    private ItemListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        mProductName = getIntent().getStringExtra("Product");

        mItemListRecycler = findViewById(R.id.itemListrecyler);
        mItemaddBtn = findViewById(R.id.itemListaddBtn);

        mItemListRecycler.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mItemListRecycler.setLayoutManager(layoutManager);

        mItemList = new ArrayList<>();

        adapter = new ItemListAdapter(this, mItemList);
        mItemListRecycler.setAdapter(adapter);

        allItemslist = new ArrayList<>();
        searchItemsList = new ArrayList<>();

        mItemListAppBar = findViewById(R.id.itemlistappbar);
        setSupportActionBar(mItemListAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mProductName);

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Items").orderByChild("product_name").equalTo(mProductName);
        query.keepSynced(true);
        query.addValueEventListener(valueEventListener);

        Query searchquery = FirebaseDatabase.getInstance().getReference().child("Items");
        searchquery.addValueEventListener(searchvalueEventListener);

        mItemaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddItemIntent = new Intent(ItemListActivity.this, AddItemActivity.class);
                AddItemIntent.putExtra("product_name", mProductName);
                startActivity(AddItemIntent);
            }
        });

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

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {

                if (newText.equals("")) {
                    mItemList.clear();
                    for (Item item : allItemslist) {
                        mItemList.add(item);
                    }
                    adapter.notifyDataSetChanged();

                    return true;
                } else {
                    searchItemsList.clear();
                    for (Item item : allItemslist) {
                        if (!TextUtils.isEmpty(item.itemName) && !TextUtils.isEmpty(item.itemDesc)) {
                            if (item.itemName.toLowerCase().contains(newText) || item.itemDesc.toLowerCase().contains(newText)) {
                                searchItemsList.add(item);
                            }
                        }
                    }
                    mItemList.clear();
                    for (Item item : searchItemsList) {
                        mItemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }

            public boolean onQueryTextSubmit(final String newText) {

                if (newText.equals("")) {
                    mItemList.clear();
                    for (Item item : allItemslist) {
                        mItemList.add(item);
                    }
                    adapter.notifyDataSetChanged();

                    return true;
                } else {
                    searchItemsList.clear();
                    for (Item item : allItemslist) {
                        if (!TextUtils.isEmpty(item.itemName) && !TextUtils.isEmpty(item.itemDesc)) {
                            if (item.itemName.toLowerCase().contains(newText) || item.itemDesc.toLowerCase().contains(newText)) {
                                searchItemsList.add(item);
                            }
                        }
                    }
                    mItemList.clear();
                    for (Item item : searchItemsList) {
                        mItemList.add(item);
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
            mItemList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    mItemList.add(item);

                }
                adapter.notifyDataSetChanged();

            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener searchvalueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            allItemslist.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    allItemslist.add(item);

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private class ItemListAdapter extends RecyclerView.Adapter<ItemListViewHolder> {

        private Context mctx;
        private List<Item> itemList;

        public ItemListAdapter(Context mctx, List<Item> itemList) {
            this.mctx = mctx;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mctx).inflate(R.layout.single_item_layout, parent, false);
            return new ItemListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemListViewHolder itemListViewHolder, final int i) {
            final Item item = mItemList.get(i);
            itemListViewHolder.itemOneDesc.setText(item.itemOneDesc);
            itemListViewHolder.itemname.setText(item.itemName);
            Picasso.get().load(item.image).into(itemListViewHolder.itemImage);

            itemListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemnamestring = mItemList.get(i).itemName;

                    Query itemref = FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("itemName").equalTo(itemnamestring);
                    itemref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String itemrefstring = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                            Log.d("itemkey", itemrefstring);
                            Intent ItemPageIntent = new Intent(ItemListActivity.this, ItemPageActivity.class);
                            ItemPageIntent.putExtra("product_name", mProductName);
                            ItemPageIntent.putExtra("itemName", mItemList.get(i).itemName);
                            ItemPageIntent.putExtra("itemRef", itemrefstring);
                            ItemPageIntent.putExtra("itemDesc", mItemList.get(i).itemDesc);
                            ItemPageIntent.putExtra("itemOneDesc", mItemList.get(i).itemOneDesc);
                            ItemPageIntent.putExtra("image", mItemList.get(i).image);
                            startActivity(ItemPageIntent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            itemListViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder delete = new AlertDialog.Builder(ItemListActivity.this);
                    CharSequence[] options = new CharSequence[]{"Edit"};
                    delete.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (which == 0) {

                                String itemnamestring = mItemList.get(i).itemName;

                                Query itemref = FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("itemName").equalTo(itemnamestring);
                                itemref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                        String itemrefstring = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                        Log.d("itemkey", itemrefstring);
                                        Intent AddItemIntent = new Intent(ItemListActivity.this, AddItemActivity.class);
                                        AddItemIntent.putExtra("product_name", mProductName);
                                        AddItemIntent.putExtra("itemName", mItemList.get(i).itemName);
                                        AddItemIntent.putExtra("itemRef", itemrefstring);
                                        AddItemIntent.putExtra("itemDesc", mItemList.get(i).itemDesc);
                                        AddItemIntent.putExtra("itemOneDesc", mItemList.get(i).itemOneDesc);
                                        AddItemIntent.putExtra("image", mItemList.get(i).image);
                                        startActivity(AddItemIntent);
                                        finish();
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
            });
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

    private class ItemListViewHolder extends RecyclerView.ViewHolder {

        TextView itemname, itemOneDesc;
        ImageView itemImage;

        public ItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.itemname);
            itemOneDesc = itemView.findViewById(R.id.onelinedesc);
            itemImage = itemView.findViewById(R.id.productimage);
        }
    }
}
