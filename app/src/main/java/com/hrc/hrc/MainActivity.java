package com.hrc.hrc;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

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

        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AboutUsIntent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(AboutUsIntent);
            }
        });

        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ContactUsIntent = new Intent(MainActivity.this, ContactUsActivity.class);
                startActivity(ContactUsIntent);
            }
        });

    }


}
