package com.example.android.otpverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button signupButton;
    Button loginButton;
    TextInputLayout username;
    TextInputLayout password;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));

        signupButton = findViewById(R.id.sign_up);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateUsername() & validatePassword()){
                    doesUserExist();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                Intent intent1 = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    boolean validateUsername(){

        String val = username.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    boolean validatePassword(){

        String val = password.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    void doesUserExist(){
        String inputPhoneNo = username.getEditText().getText().toString().trim();
        String inputPassword = password.getEditText().getText().toString().trim();


        Query checkUser = reference.orderByChild("phoneNo").equalTo(inputPhoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(inputPhoneNo).child("password").getValue(String.class);

                    if(passwordFromDB.equals(inputPassword)){

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(inputPhoneNo).child("fullName").getValue(String.class);
                        String usernameFromDB = snapshot.child(inputPhoneNo).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(inputPhoneNo).child("email").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(inputPhoneNo).child("phoneNo").getValue(String.class);

                        Intent intent = new Intent(MainActivity.this, CatalogActivity.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else{
                    username.setError("No such user exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}