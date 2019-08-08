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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private Button muploadimagebtn, mDeleteImagebtn, maddProdbtn;
    private TextView resultdisplay;
    private String imagestring, prodnamestring, downloadUrl;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mproductName = findViewById(R.id.prod_name_input);
        resultdisplay = findViewById(R.id.result);
        appbar = findViewById(R.id.addprod_app_bar);

        muploadimagebtn = findViewById(R.id.uploadimagebtn);
        mDeleteImagebtn = findViewById(R.id.deleteimagebtn);
        maddProdbtn = findViewById(R.id.addProdBtn);

        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Product");

        muploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddProductActivity.this);
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
                        resultdisplay.setText("no image");
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
                prodnamestring = mproductName.getEditText().getText().toString();
                HashMap<String, Object> result = new HashMap<>();

                if (!mproductName.equals("")) {
                    result.put("product_name", prodnamestring);
                }
                result.put("image",imagestring);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Product").child(prodnamestring);
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


                Intent mainintent = new Intent(AddProductActivity.this, ProductActivity.class);
                startActivity(mainintent);
            }

        });


        prodnamestring = mproductName.getEditText().getText().toString();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            prodnamestring = mproductName.getEditText().getText().toString();

            if (prodnamestring == "") {
                Toast.makeText(this, "please enter name", Toast.LENGTH_LONG).show();

            } else {
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

                    StorageReference filepath = mImageStorage.child("Product").child(prodnamestring + ".jpg");

                    UploadTask uploadTask = filepath.putBytes(thumb_byte);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                mImageStorage.child("Product").child(prodnamestring + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        downloadUrl = uri.toString();
                                        if (!downloadUrl.equals("")) {
                                            imagestring = downloadUrl;
                                            mProgressDialaog.dismiss();
                                        }
                                        mProgressDialaog.dismiss();
                                        resultdisplay.setText(downloadUrl);
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
}
