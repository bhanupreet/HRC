package com.hrc.hrc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    private CardView mAmarjit, mHarpal, mKaran;
    private TextView moffadd, moffph, mgodph1, mgodph2, mgodadd, memail;
    private Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mAmarjit = findViewById(R.id.Amarjit_Singh);
        mHarpal = findViewById(R.id.Harpal_Singh);
        mKaran = findViewById(R.id.karandeep_singh);
        moffadd = findViewById(R.id.officeadd);
        moffph = findViewById(R.id.officeph);
        mgodph1 = findViewById(R.id.godownph1);
        mgodph2 = findViewById(R.id.godownph2);
        mgodadd = findViewById(R.id.godownadd);
        memail = findViewById(R.id.email);
        appbar = findViewById(R.id.contactusappbar);

        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAmarjit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9811065155"));
                startActivity(intent);
            }
        });

        mHarpal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9811093181"));
                startActivity(intent);
            }
        });

        mKaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9999085959"));
                startActivity(intent);
            }
        });

        moffph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 23862334"));
                startActivity(intent);
            }
        });

        mgodph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 23869199"));
                startActivity(intent);
            }
        });

        mgodph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 23865155"));
                startActivity(intent);
            }
        });

        memail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:hrc485@gmail.com"));
                intent.putExtra(Intent.EXTRA_EMAIL, "hrc485@gmail.com");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
