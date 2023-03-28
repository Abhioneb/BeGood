package com.example.begood;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Random;
import java.util.UUID;

public class ImageUpload extends AppCompatActivity {

    public Uri filePath;
    public Bitmap bitmap;
    EditText caption;
    Button addPhoto, post;
    ImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        caption = findViewById(R.id.caption);
        photo = findViewById(R.id.photo);
        addPhoto = findViewById(R.id.addPhoto);
        post = findViewById(R.id.post);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadToFirebase();
            }

        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(ImageUpload.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                        withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();


            }

        });

        caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();

                if (length < 20) {
                    caption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    caption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                }
            }
        });
    }

    public void uploadToFirebase() {


        ProgressDialog dialog = new ProgressDialog(ImageUpload.this);
        dialog.setTitle("Sharing Your Request");
        dialog.show();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("image" + UUID.randomUUID().toString());

        storageReference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                dialog.dismiss();


//                                UUIDs are generated using a combination of the current timestamp, a
//                                unique identifier for the computer generating the UUI and other factors such as the
//                                current network address. The result is a unique value that is extremely unlikely to be duplicated.

                                // It generates a unique ID for the post
                                DatabaseReference mpostsRef = FirebaseDatabase.getInstance().getReference("posts");
                                String postId = mpostsRef.push().getKey();

                                // Gets the current timestamp which is used to calculate timeSpent since post was made.
                                long timestamp = System.currentTimeMillis();

                                // user is logged in ,so, get his userId
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = user.getUid();

                                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                mDatabase.getReference("users").child(user.getUid()).child("userName")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // Get the username value from the snapshot
                                                String username = dataSnapshot.getValue(String.class);
                                                posts post = new posts(postId, username, userId
                                                        , caption.getText().toString(), getIntent().getStringExtra("requestType"), getIntent().getDoubleExtra("latitudeSent", 25.473034), getIntent().getDoubleExtra("longitudeSent", 81.878357),
                                                        getIntent().getStringExtra("address"), timestamp, uri.toString());

                                                // Save the new post to the Realtime Database

                                                mpostsRef.child(postId).setValue(post);

                                                Toast.makeText(ImageUpload.this, "Post Shared", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(ImageUpload.this, username, Toast.LENGTH_SHORT).show();
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                            }
                        });


                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded:" + (int) percent + "%");

                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                photo.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}










