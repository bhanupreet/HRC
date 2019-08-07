package com.hrc.hrc;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainAppbar;
    private Button aboutUsBtn, productsBtn, contactUsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsBtn = findViewById(R.id.main_productsBtn);
        aboutUsBtn = findViewById(R.id.main_AboutUsBtn);
        contactUsBtn = findViewById(R.id.main_ContactUsBtn);

        mainAppbar = findViewById(R.id.mainAppbar);

        setSupportActionBar(mainAppbar);
        getSupportActionBar().setTitle(R.string.home);

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProductIntent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(ProductIntent);
            }
        });
    }


}
