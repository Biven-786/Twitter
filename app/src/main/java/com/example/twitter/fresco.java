package com.example.twitter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class fresco extends AppCompatActivity {
    private SimpleDraweeView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_fresco);
        img=findViewById(R.id.my_image_view);
        img.setImageURI("https://images.unsplash.com/photo-1555083892-97490c72c90c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&w=1000&q=80");
    }
}