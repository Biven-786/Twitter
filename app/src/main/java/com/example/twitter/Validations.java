package com.example.twitter;



import android.text.Editable;
import android.text.TextWatcher;


import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {

    public static Validations instance;

    public static Validations getInstance() {
        if (instance == null) {
            instance = new Validations();
        }
        return instance;
    }

    public TextWatcher setFalse(TextInputLayout view){
        android.text.TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        return textWatcher;
    }



    public String passwordvalid(String pass) {

        if (pass.isEmpty()) {
            return  "Enter Password";
        }
        else if (!isValidPassword(pass)) {
            return "Enter Valid Password";
        }
        return  null;
    }

    public String emailvalid(String str) {


        if (str.isEmpty()) {
            return "Enter Email";
        } else if (!str.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")) {
            return "Wrong Email";
        }
        return null;
    }

    private boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

}
