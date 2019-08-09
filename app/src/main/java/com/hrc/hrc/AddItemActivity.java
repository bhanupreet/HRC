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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AddItemActivity extends AppCompatActivity {

    private TextInputLayout mItemName, mItemDesc, mItemOneDesc;
    private Button muploadimagebtn, mDeleteImagebtn, madditembtn, mDeletebtn;
    private TextView resultdisplay;
    private String imagestring = "", prodnamestring, downloadUrl, itemstring, itemrefstring;
    private ProgressDialog mProgressDialaog;
    private StorageReference mImageStorage;
    private Toolbar appbar;
    private String mItemDescString, mItemOneDescString;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        prodnamestring = getIntent().getStringExtra("product_name");
        itemstring = getIntent().getStringExtra("itemName");
        itemrefstring = getIntent().getStringExtra("itemRef");
        mItemDescString = getIntent().getStringExtra("itemDesc");
        mItemOneDescString = getIntent().getStringExtra("itemOneDesc");
        imagestring = getIntent().getStringExtra("image");

//        itemrefstring = FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("itemName").equalTo(itemstring).getRef().getKey();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mItemName = findViewById(R.id.item_name_input);
        resultdisplay = findViewById(R.id.additemresult);
        muploadimagebtn = findViewById(R.id.additemuploadimagebtn);
        mDeleteImagebtn = findViewById(R.id.additemdeleteimagebtn);
        madditembtn = findViewById(R.id.additemBtn);
        appbar = findViewById(R.id.additem_app_bar);
        mDeletebtn = findViewById(R.id.deleteitemBtn);
        mItemDesc = findViewById(R.id.item_desc_input);
        mItemOneDesc = findViewById(R.id.item_descone_input);

        if(TextUtils.isEmpty(imagestring)){
            mDeleteImagebtn.setClickable(false);
            mDeleteImagebtn.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(itemstring)) {
            mItemName.getEditText().setText(itemstring);
            mDeletebtn.setVisibility(View.VISIBLE);
            mDeletebtn.setClickable(true);
            madditembtn.setText("Update");
        }
        if (!TextUtils.isEmpty(imagestring)) {
            resultdisplay.setText(imagestring);
            mDeleteImagebtn.setClickable(true);
            mDeleteImagebtn.setVisibility(View.VISIBLE);

        }
        if(!TextUtils.isEmpty(mItemDescString)){
            mItemDesc.getEditText().setText(mItemDescString);

        }
        if(!TextUtils.isEmpty(mItemOneDescString)){
            mItemOneDesc.getEditText().setText(mItemOneDescString);
        }

        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Item");

        muploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemstring = mItemName.getEditText().getText().toString();
                if (itemstring.equals("")) {
                    Toast.makeText(AddItemActivity.this, "Please enter name", Toast.LENGTH_LONG).show();

                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(AddItemActivity.this);
                }
            }
        });


        mDeleteImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddItemActivity.this);

                alert.setTitle("Delete Image");
                alert.setMessage("Are you sure you want to delete the image?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        StorageReference filepath = mImageStorage.child("Items").child(itemstring + ".jpg");
                        filepath.delete();
                        // continue with delete
                        imagestring = "default image";
                        resultdisplay.setText("");
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

        madditembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemstring = mItemName.getEditText().getText().toString();
                mItemDescString = mItemDesc.getEditText().getText().toString();
                mItemOneDescString = mItemOneDesc.getEditText().getText().toString();
                HashMap<String, Object> result = new HashMap<>();

                if (!mItemName.equals("")) {
                    result.put("itemName", itemstring);
                }
                if (!mItemOneDescString.equals("")) {
                    result.put("itemOneDesc", mItemOneDescString);
                }
                if (!mItemDescString.equals("")) {
                    result.put("itemDesc", mItemDescString);
                }
                result.put("image", imagestring);

                result.put("product_name", prodnamestring);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                if (TextUtils.isEmpty(itemrefstring)) {
                    myRef = database.getReference().child("Items").push();

                } else {
                    myRef = database.getReference().child("Items").child(itemrefstring);
                }
                myRef.updateChildren(result).

                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(AddItemActivity.this, "details added successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(AddItemActivity.this, "an error occured while uploadig data", Toast.LENGTH_SHORT).show();

                                }
                                // mProgressDialaog.dismiss();

                            }
                        });


                itemrefstring = myRef.getKey();
                Intent mainintent = new Intent(AddItemActivity.this, ItemListActivity.class);
                mainintent.putExtra("Product", prodnamestring);
                startActivity(mainintent);
            }

        });

        mDeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            itemstring = mItemName.getEditText().getText().toString();

            if (TextUtils.isEmpty(itemstring)) {
                Toast.makeText(this, "please enter name", Toast.LENGTH_LONG).show();

            } else {
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    mProgressDialaog = new ProgressDialog(AddItemActivity.this);
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

                    StorageReference filepath = mImageStorage.child("Items").child(itemstring + ".jpg");

                    UploadTask uploadTask = filepath.putBytes(thumb_byte);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                mImageStorage.child("Items").child(itemstring + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        downloadUrl = uri.toString();
                                        if (!downloadUrl.equals("")) {
                                            imagestring = downloadUrl;
                                            mProgressDialaog.dismiss();
                                        }
                                        mProgressDialaog.dismiss();
                                        resultdisplay.setText(downloadUrl);
                                        mDeleteImagebtn.setClickable(true);
                                        mDeleteImagebtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(AddItemActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
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
