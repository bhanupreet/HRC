package com.hrc.hrc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemPageActivity extends AppCompatActivity {

    String prodnamestring,itemstring,itemrefstring,mItemDescString,mItemOneDescString,imagestring;
    private TextView mItemName, mItemDesc;
    private ImageView mItemImage;
    private Toolbar mitempage_appbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        prodnamestring = getIntent().getStringExtra("product_name");
        itemstring = getIntent().getStringExtra("itemName");
        itemrefstring = getIntent().getStringExtra("itemRef");
        mItemDescString = getIntent().getStringExtra("itemDesc");
        mItemOneDescString = getIntent().getStringExtra("itemOneDesc");
        imagestring = getIntent().getStringExtra("image");

        mItemName = findViewById(R.id.itempage_itemname);
        mItemDesc = findViewById(R.id.itempage_desc);
        mItemImage = findViewById(R.id.itempage_image);
        mitempage_appbar = findViewById(R.id.itempage_appbar);

        setSupportActionBar(mitempage_appbar);
        getSupportActionBar().setTitle(itemstring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Picasso.get().load(imagestring).into(mItemImage);
        mItemName.setText(itemstring);
        mItemDesc.setText(mItemDescString);
        mItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullscreenIntent = new Intent(ItemPageActivity.this,FullScreenImageActivity.class);
                fullscreenIntent.putExtra("product_name", prodnamestring);
                fullscreenIntent.putExtra("itemName", itemstring);
                fullscreenIntent.putExtra("itemRef", itemrefstring);
                fullscreenIntent.putExtra("itemDesc", mItemDescString);
                fullscreenIntent.putExtra("itemOneDesc", mItemOneDescString);
                fullscreenIntent.putExtra("image", imagestring);
                startActivity(fullscreenIntent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                Log.d("backbutton", "cheguei");
                Intent intent = new Intent(ItemPageActivity.this, ItemListActivity.class);
                intent.putExtra("Product", prodnamestring);
                startActivity(intent);
                break;
        }
        return true;
    }

}
