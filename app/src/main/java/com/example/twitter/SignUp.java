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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    //private static final String TAG = "MainActivity";
    //private TextView text;
    //private SharedPref shr;
    private TextInputLayout name, email, password;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;

    private Validations val;
    private TextInputEditText emailEdit, fullName;
    private SwitchCompat s;
    private CoordinatorLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        findViewById(R.id.button2).setOnClickListener(view -> onBackPressed());

        rootLayout = findViewById(R.id.activity_sign_up_root_layout);
        name = findViewById(R.id.inputlayout_fullname);

        //shr = SharedPref.getInstance(this);

        s = findViewById(R.id.remember);
        emailEdit = findViewById(R.id.email);
        fullName = findViewById(R.id.fullname);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


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
    }

    private void signup() {

        if (!email.isErrorEnabled() && !password.isErrorEnabled()) {
            //Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                    .addOnSuccessListener(authResult -> {
                        save();
                        Toast.makeText(this, "Registered Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, Home.class);
                        //intent.putExtra("Myfirst Program", name.getEditText().getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Snackbar.make(rootLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });

        }


        /*if(s.isChecked()) {
            shr.saveString(shr.EMAIL, email.getEditText().getText().toString());
            shr.saveString(shr.NAME, name.getEditText().getText().toString());
            shr.saveBoolean(shr.Switch, s.isChecked());
        }else {
            shr.clearData();
        }*/
    }

    private void save() {
        db = FirebaseFirestore.getInstance();
        Map<String, String> data = new HashMap<>();
        data.put("Name", name.getEditText().getText().toString().trim());
        data.put("Email", email.getEditText().getText().toString().trim());
        data.put("Password", password.getEditText().getText().toString().trim());
        db.collection("User").document(mAuth.getCurrentUser().getUid()).set(data).addOnSuccessListener(authResult -> {
            Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e->{
            e.printStackTrace();
        });
    }




    /*findViewById(R.id.button).setOnClickListener(view -> {
            startActivity(new Intent(this,MainActivity2.class).putExtra("Myfirst Program",name.getEditText().getText().toString()));

        });
        parceable p=new parceable();
        p.str="Hello!";
        Log.d(TAG, "onCreate: called "+p);
    }*/
}
