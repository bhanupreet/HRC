package com.hrc.hrc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    private Toolbar appbar;
    private TextView aboutustext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        appbar = findViewById(R.id.aboutusappbar);
        aboutustext = findViewById(R.id.aboutustext);

        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutustext.setText("About Us:\n" +
                "Harjindra Radio Components Pvt Ltd (HRC) having foot prints across Pan-India majorly covering South " +
                "India with network of more than 200+ dealers , dealing in total range of Consumer Durable Electronics," +
                " Home Appliances and most importantly their Spareparts and accessories.\n\n" +
                "The company was founded by Late S.Gurdip Singh with his brother S.Amarjit Singh in 1968 starting with Radios , Transistors, " +
                "printed circuit boards and transformers.\n \n" +
                "With changing scenario from last 5 decades the company has evolved with time in various electronics and electrical products.");

    }
}
