package com.example.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private static final String TAG = "SignIn";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextInputLayout name, email, password;
    private SharedPref shr;
    private Validations val;
    private TextInputEditText emailEdit, fullName;
    private SwitchCompat s;
    private CoordinatorLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        rootLayout = findViewById(R.id.activity_sign_in_root_layout);

        findViewById(R.id.activity_sign_in_sign_up_bu).setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
        });
        
        //shr = SharedPref.getInstance(this);
        //name = findViewById(R.id.inputlayout_fullname);
        s = findViewById(R.id.remember);
        emailEdit = findViewById(R.id.email);
        fullName = findViewById(R.id.fullname);

        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        
        val = Validations.getInstance();

        email = findViewById(R.id.inputlayout_email);
        password = findViewById(R.id.inputlayout_password);

        password.getEditText().addTextChangedListener(val.setFalse(password));
        email.getEditText().addTextChangedListener(val.setFalse(email));

        findViewById(R.id.button).setOnClickListener(arg0 -> {

            String str = val.emailvalid(email.getEditText().getText().toString().trim());
            email.setError(str);
            String str1 = val.passwordvalid(password.getEditText().getText().toString().trim());
            password.setError(str1);
            signup();

        });
        load();
    }

    private void load() {
    }
/*

    private void load() {

        boolean bol = shr.loadBooleanData(shr.Switch);
        if (bol) {
            emailEdit.setText(shr.loadStringData(shr.EMAIL));
            //fullName.setText(shr.loadStringData(shr.NAME));
        }
    }
*/

    private void signup() {

        if (!email.isErrorEnabled() && !password.isErrorEnabled()) {
            mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                    .addOnSuccessListener(authResult -> {

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, Home.class);
                        //intent.putExtra("Myfirst Program", email.getEditText().getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Snackbar.make(rootLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        }
        
        
        /*if (s.isChecked()) {
            shr.saveString(shr.EMAIL, email.getEditText().getText().toString());
            //shr.saveString(shr.NAME, name.getEditText().getText().toString());
            shr.saveBoolean(shr.Switch, s.isChecked());
        } else {
            shr.clearData();
        }*/
    }


}