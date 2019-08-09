package com.hrc.hrc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    private PhotoView fullscreenphoto;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private String itemstring,imagestring,prodnamestring,itemrefstring,mItemDescString,mItemOneDescString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);


        prodnamestring = getIntent().getStringExtra("product_name");
        itemstring = getIntent().getStringExtra("itemName");
        itemrefstring = getIntent().getStringExtra("itemRef");
        mItemDescString = getIntent().getStringExtra("itemDesc");
        mItemOneDescString = getIntent().getStringExtra("itemOneDesc");
        imagestring = getIntent().getStringExtra("image");

        fullscreenphoto = findViewById(R.id.fullscreenimage);
        mToolbar = findViewById(R.id.fullscreenimagappbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(itemstring);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ItemPageIntent = new Intent(FullScreenImageActivity.this, ItemPageActivity.class);
                ItemPageIntent.putExtra("product_name", prodnamestring);
                ItemPageIntent.putExtra("itemName", itemstring);
                ItemPageIntent.putExtra("itemRef", itemrefstring);
                ItemPageIntent.putExtra("itemDesc", mItemDescString);
                ItemPageIntent.putExtra("itemOneDesc", mItemOneDescString);
                ItemPageIntent.putExtra("image", imagestring);
                startActivity(ItemPageIntent);

            }
        });

        Picasso.get().load(imagestring).into(fullscreenphoto);
    }
}
