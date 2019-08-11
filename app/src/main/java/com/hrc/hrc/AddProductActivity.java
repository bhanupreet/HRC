package com.hrc.hrc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AddProductActivity extends AppCompatActivity {

    private TextInputLayout mproductName;
    private Button muploadimagebtn, mDeleteImagebtn, maddProdbtn, mDeleteProdbtn;
    private TextView resultdisplay;
    private String imagestring, prodnamestring, downloadUrl, prodrefstring, newprodnamestring;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private Toolbar appbar;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        prodnamestring = getIntent().getStringExtra("product_name");
        prodrefstring = getIntent().getStringExtra("prod_ref");
        imagestring = getIntent().getStringExtra("image");

        mproductName = findViewById(R.id.prod_name_input);
        resultdisplay = findViewById(R.id.result);
        appbar = findViewById(R.id.addprod_app_bar);
        mDeleteProdbtn = findViewById(R.id.deleteprodBtn);
        muploadimagebtn = findViewById(R.id.uploadimagebtn);
        mDeleteImagebtn = findViewById(R.id.deleteimagebtn);
        maddProdbtn = findViewById(R.id.addProdBtn);

        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Product");

        mDeleteImagebtn.setVisibility(View.INVISIBLE);
        mDeleteImagebtn.setClickable(false);

        if (!TextUtils.isEmpty(prodnamestring)) {
            mproductName.getEditText().setText(prodnamestring);
            maddProdbtn.setText("Update");
            mDeleteProdbtn.setClickable(true);
            mDeleteProdbtn.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(imagestring)) {
            resultdisplay.setText(imagestring);
            mDeleteImagebtn.setVisibility(View.VISIBLE);
            mDeleteImagebtn.setClickable(true);
        }

        muploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newprodnamestring = mproductName.getEditText().getText().toString();
                if (newprodnamestring.equals("")) {
                    Toast.makeText(AddProductActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AddProductActivity.this);
                }
            }
        });

        mDeleteImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddProductActivity.this);
                alert.setTitle("Delete Image");
                alert.setMessage("Are you sure you want to delete the image?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        imagestring = "default image";
                        resultdisplay.setText("default image");
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
        maddProdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newprodnamestring = mproductName.getEditText().getText().toString();
                prodrefstring = myRef.getKey();

                HashMap<String, Object> result = new HashMap<>();
                if (!TextUtils.isEmpty(newprodnamestring)) {
                    result.put("itemName", newprodnamestring);
                }
                if (!TextUtils.isEmpty(imagestring)) {
                    result.put("image", imagestring);
                } else {
                    result.put("image", "default image");
                }
                myRef.updateChildren(result).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddProductActivity.this, "details added successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(AddProductActivity.this, "an error occured while uploadig data", Toast.LENGTH_SHORT).show();
                                }
                                // mProgressDialaog.dismiss();
                            }
                        });
                prodrefstring = myRef.getKey();

                if (!TextUtils.isEmpty(prodnamestring)) {
                    FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("product_name").equalTo(prodnamestring).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    FirebaseDatabase.getInstance().getReference().child("Items").child(key).child("product_name").setValue(newprodnamestring);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Intent mainintent = new Intent(AddProductActivity.this, ProductActivity.class);
                startActivity(mainintent);
            }

        });

        mDeleteProdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddProductActivity.this);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        FirebaseDatabase.getInstance().getReference().child("Product").child(prodrefstring).removeValue();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference desertRef = storageRef.child("Product/" + prodrefstring + ".jpg");
                        desertRef.delete();
//code to delete items


                        FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("product_name").equalTo(prodrefstring).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String key = snapshot.getKey();
                                        String itemName = snapshot.child("itemName").getValue().toString();

                                        FirebaseDatabase.getInstance().getReference().child("Items").child(key).removeValue();
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference desertRef = storageRef.child("Items/" + key + ".jpg");
                                        desertRef.delete();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


//code to delete item end

                        Intent prodIntent = new Intent(AddProductActivity.this, ProductActivity.class);
                        startActivity(prodIntent);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialaog = new ProgressDialog(AddProductActivity.this);
                mProgressDialaog.setMessage("Please wait while we upload and process the image");
                mProgressDialaog.setTitle("Uploading Image...");
                mProgressDialaog.setCanceledOnTouchOutside(false);
                mProgressDialaog.show();
                Uri resultUri = result.getUri();
                File thumb_filepath = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(70)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                byte[] thumb_byte = baos.toByteArray();
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                if (TextUtils.isEmpty(prodrefstring)) {
                    myRef = database.getReference().child("Product").push();
                } else {
                    myRef = database.getReference().child("Product").child(prodrefstring);
                }
                prodrefstring = myRef.getKey();
                StorageReference filepath = mImageStorage.child("Product").child(prodrefstring + ".jpg");
                UploadTask uploadTask = filepath.putBytes(thumb_byte);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            mImageStorage.child("Product").child(prodrefstring + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                    if (!downloadUrl.equals("")) {
                                        imagestring = downloadUrl;
                                        mProgressDialaog.dismiss();
                                    }
                                    mProgressDialaog.dismiss();
                                    resultdisplay.setText(downloadUrl);
                                    mDeleteImagebtn.setVisibility(View.VISIBLE);
                                    mDeleteImagebtn.setClickable(true);
                                    Toast.makeText(AddProductActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                mProgressDialaog.dismiss();
            }

        }
    }
}
