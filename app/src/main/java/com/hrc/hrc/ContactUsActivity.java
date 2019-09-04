package com.hrc.hrc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    private CardView mAmarjit, mHarpal, mKaran;
    private TextView moffadd, moffph, mgodph1, mgodph2, mgodadd, memail;
    private Toolbar appbar;
    private Button mAboutUsbtn, mProductsBtn;

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
        mAboutUsbtn = findViewById(R.id.contact_AboutUsBtn);
        mProductsBtn = findViewById(R.id.contactus_productsBtn);

        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAmarjit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9811065155"));
                startActivity(intent);
                finish();
            }
        });

        mHarpal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9811093181"));
                startActivity(intent);
                finish();
            }
        });

        mKaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9999085959"));
                startActivity(intent);
                finish();
            }
        });

        moffph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 23862334"));
                startActivity(intent);
                finish();
            }
        });

        mgodph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 23869199"));
                startActivity(intent);
                finish();
            }
        });

        mgodph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011 23865155"));
                finish();

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
                    finish();
                }
            }
        });

        mAboutUsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AboutUsIntent = new Intent(ContactUsActivity.this, AboutUsActivity.class);
                startActivity(AboutUsIntent);
                finish();
            }
        });

        mProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AboutUsIntent = new Intent(ContactUsActivity.this, ProductActivity.class);
                startActivity(AboutUsIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ContactUsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
