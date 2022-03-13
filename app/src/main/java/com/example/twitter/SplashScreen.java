package com.example.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_x);

        Fresco.initialize(this.getApplicationContext());

        final ConstraintLayout startLayout = findViewById(R.id.splashScreenStartRootLayout);

        final ConstraintSet conSetFinal = new ConstraintSet();
        conSetFinal.clone(this, R.layout.activity_splash_screen);

        /*new Handler().postDelayed(() -> {
            TransitionManager.beginDelayedTransition(startLayout);
            constraints[0] = conSetFinal;
            constraints[0].applyTo(startLayout);
        },50);*/
        Glide.with(this).load(R.drawable.logo).override(500,500).into((ImageView) findViewById(R.id.imageView1));
        mAuth = FirebaseAuth.getInstance();
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(100);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        TransitionManager.beginDelayedTransition(startLayout);
                        conSetFinal.applyTo(startLayout);
                    }, 50);
                    sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    currentUser = mAuth.getCurrentUser();
                    Intent intent;
                    if (currentUser != null) {
                        intent = new Intent(SplashScreen.this, Home.class);
                    } else {
                        intent = new Intent(SplashScreen.this, SignIn.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
        thread.start();

    }
}