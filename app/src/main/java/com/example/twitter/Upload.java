package com.example.twitter;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;

public class Upload extends AppCompatActivity {
    private static final String TAG = "Upload";

    private Button upload, save;
    private SimpleDraweeView img;
    private StorageReference storageRef;
    private String location;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String id;

    private final ActivityResultLauncher<String> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission()
            , resul -> {
                if (resul) {
                    Log.d(TAG, "registerForActivityResult: Permission Granted");
                } else {
                    Log.e(TAG, "registerForActivityResult: Permission denied");
                }

            });

    private final ActivityResultLauncher<String> someActivity = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result!=null && result.getPath() != null) {
                    img.setImageURI(result.toString());
                    // Picasso.get().load(result.toString()).resize(500, 500).centerCrop().into(img);

                    Log.d(TAG, "registerForActivityResult " + result.getPath());
                    Log.d(TAG, "registerForActivityResult " + result.describeContents());
                    uploadFile(result.toString());
                } else {
                    Log.e(TAG, "regis" +
                            "terForActivityResult denied ");
                }
            }
    );

    private final ActivityResultLauncher<String[]> someActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            results -> {
                String[] keys = results.keySet().toArray(new String[3]);
                for (int i = 0; i < results.keySet().size(); i++) {
                    switch (keys[i]) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                        case Manifest.permission.CAMERA: {
                            Log.d(TAG, "someActivityLauncher registerForActivityResult: Permission Granted");
                            break;
                        }
                        default: {
                            Log.d(TAG, "someActivityLauncher registerForActivityResult: Permission Denied");
                            break;
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        id = mAuth.getCurrentUser().getUid();

        img = findViewById(R.id.my_image_view);
        upload = findViewById(R.id.UPLOAD);
        save = findViewById(R.id.SAVE);


        String[] st = new String[3];
        st[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        st[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        st[2] = Manifest.permission.CAMERA;

        /*upload.setOnClickListener(view -> {
                    someActivityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                });*/
        upload.setOnClickListener(view -> {
            someActivityLauncher.launch(st);
            open();
        });

        FirebaseStorage fireStorage = FirebaseStorage.getInstance();
        storageRef = fireStorage.getReference(RandomString());

        save.setOnClickListener(view -> {
            onBackPressed();
            Toast.makeText(Upload.this, "Image Uploaded Sucessfully", Toast.LENGTH_SHORT).show();

        });

    }


    private String RandomString() {

        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    private void open() {
        someActivity.launch("image/*");
    }

    private void uploadFile(String path) {
        try {
            UploadTask upload = storageRef.putFile(Uri.parse(path));
            upload.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    exception.printStackTrace();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {

                        //Url saved
                        location = uri.toString();
                        Log.d(TAG, "onSuccess: taskSnapshot " + location);


                        //Image Link Upload
                        DocumentReference doc = firestore.collection("User").document(id);
                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put("URL", location);

                        doc.update(updateMap).addOnSuccessListener(up -> {
                            Log.d(TAG, "uploadFile: " + location);
                        });
                        save.setEnabled(true);
                    });

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}