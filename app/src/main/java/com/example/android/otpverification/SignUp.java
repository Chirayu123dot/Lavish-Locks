package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout username;
    TextInputLayout email;
    TextInputLayout phoneNo;
    TextInputLayout password;
    Button signupButton;
    Button loginButton;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));

        name = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phone_no);
        password = findViewById(R.id.password);
        signupButton = findViewById(R.id.sign_up);
        loginButton = findViewById(R.id.login);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateName() & validateUsername() & validateEmail() & validatePhoneNo() & validatePassword()){
                    String mFullName = name.getEditText().getText().toString().trim();
                    String mUsername = username.getEditText().getText().toString().trim();
                    String mEmail = email.getEditText().getText().toString().trim();
                    String mPhoneNo = phoneNo.getEditText().getText().toString().trim();
                    String mPassword = password.getEditText().getText().toString().trim();


                    UserHelperClass user = new UserHelperClass(mFullName, mUsername, mEmail, mPhoneNo, mPassword);

                    reference.child(mPhoneNo).setValue(user);

                    Intent intent = new Intent(SignUp.this, UserProfile.class);
                    intent.putExtra("name", mFullName);
                    intent.putExtra("username", mUsername);
                    intent.putExtra("email", mEmail);
                    intent.putExtra("phoneNo", mPhoneNo);
                    intent.putExtra("password", mPassword);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    boolean validateName(){
        if(TextUtils.isEmpty(name.getEditText().getText().toString().trim())){
            name.setError("Field cannot be empty");
            return false;
        }
        else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    boolean validateUsername(){

        String val = username.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }
        else if(val.length() >= 15){
            username.setError("Username too long");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    boolean validateEmail(){
        String val = email.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    boolean validatePhoneNo(){
        String val = phoneNo.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            phoneNo.setError("Field cannot be empty");
            return false;
        }
        else{
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }

    boolean validatePassword(){

        String val = password.getEditText().getText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(passwordPattern)){
            password.setError("Password is too weak");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}
