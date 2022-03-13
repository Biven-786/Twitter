package com.example.twitter;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
 private TextView name,email,password;
 FirebaseAuth mAuth;
 FirebaseFirestore firestore;
 private ImageView img;

 String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        img=findViewById(R.id.imageView);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        /*String str=getIntent().getStringExtra("Myfirst Program");
        textView.setText(str);*/

        //To Show Data
        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        id=mAuth.getCurrentUser().getUid();
        DocumentReference doc=firestore.collection("User").document(id);
        doc.addSnapshotListener((value, error) -> {
            name.setText(value.getString("Name"));
            email.setText(value.getString("Email"));
            password.setText(value.getString("Password"));
            Glide.with(this).load(value.getString("URL")).override(500,500).circleCrop().into(img);
        });



        findViewById(R.id.button).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(Home.this,SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Toast.makeText(Home.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        findViewById(R.id.upload).setOnClickListener(view -> {
            Intent inten=new Intent(Home.this,Upload.class);
            startActivity(inten);
        });
    }
}