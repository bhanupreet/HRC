package com.hrc.hrc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {
    private String mProductName;
    private RecyclerView mItemListRecycler;
    private FloatingActionButton mItemaddBtn;
    private Toolbar mItemListAppBar;
    private List<Item> mItemList;
    private ItemListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        mProductName = getIntent().getStringExtra("Product");

        mItemListRecycler = findViewById(R.id.itemListrecyler);
        mItemaddBtn = findViewById(R.id.itemListaddBtn);

//        mItemListRecycler.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mItemListRecycler.setLayoutManager(layoutManager);

        mItemList = new ArrayList<>();

        adapter = new ItemListAdapter(this, mItemList);
        mItemListRecycler.setAdapter(adapter);


        mItemListAppBar = findViewById(R.id.itemlistappbar);
        setSupportActionBar(mItemListAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mProductName);

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Items").orderByChild("product_name").equalTo(mProductName);
        query.keepSynced(true);


        query.addValueEventListener(valueEventListener);

        mItemaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddItemIntent = new Intent(ItemListActivity.this,AddItemActivity.class);
                AddItemIntent.putExtra("product_name",mProductName);
                startActivity(AddItemIntent);
            }
        });

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
                    Intent ItemIntent = new Intent(ItemListActivity.this, ItemPageActivity.class);
                    ItemIntent.putExtra("itemname", mItemList.get(i).itemName);
                    startActivity(ItemIntent);
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
