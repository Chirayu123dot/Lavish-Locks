package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView signupButton;
    TextView loginButton;
    TextInputLayout phoneNumber;
    TextInputLayout password;
    TextInputEditText phoneNumberEditText;
    TextInputEditText passwordEditText;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
//        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));

        setSupportActionBar(findViewById(R.id.toolbar));

        signupButton = findViewById(R.id.login_activity_signup_button);
        phoneNumber = findViewById(R.id.login_activity_phone_no_text_input_layout);
        password = findViewById(R.id.login_activity_password_text_input_layout);
        loginButton = findViewById(R.id.login_activity_login_button);
        phoneNumberEditText = findViewById(R.id.login_activity_phone_no_edit_text);
        passwordEditText = findViewById(R.id.login_activity_password_edit_text);

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
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        designEditTextFields();


        userLocalStore = new UserLocalStore(this);

    }

    void designEditTextFields(){
        phoneNumber.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Phone Number" + "</i>" + "</font>"));

        phoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    phoneNumber.setEndIconVisible(true);
                    phoneNumber.setHint(" ");
                    phoneNumber.setHint("Phone Number");
                }
                else{
                    phoneNumber.setEndIconVisible(false);
                    if(TextUtils.isEmpty(phoneNumber.getEditText().getText().toString().trim())){
                        phoneNumber.setHint(" ");
                        phoneNumber.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Phone Number" + "</i>" + "</font>"));
                    }else {
                        phoneNumber.setHint(" ");
//                          phoneNumber.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "Phone Number" + "</font>"));
                        phoneNumber.setHint("Phone Number");
                    }
                }
            }
        });

        password.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Password" + "</i>" + "</font>"));
        password.setEndIconVisible(false);

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    password.setEndIconVisible(true);
                    password.setHint(" ");
                    password.setHint("Password");
                }
                else{
                    password.setEndIconVisible(false);
                    if(TextUtils.isEmpty(password.getEditText().getText().toString().trim())){
                        password.setHint(" ");
                        password.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Password" + "</i>" + "</font>"));
                    }else {
                        password.setHint(" ");
                        password.setHint("Password");
                    }
                }
            }
        });

    }
    boolean validateUsername(){

        String val = phoneNumber.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            phoneNumber.setError("Field cannot be empty");
            return false;
        }
        else{
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
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
        String inputPhoneNo = phoneNumber.getEditText().getText().toString().trim();
        String inputPassword = password.getEditText().getText().toString().trim();


        Query checkUser = reference.orderByChild("phoneNo").equalTo(inputPhoneNo);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(inputPhoneNo).child("password").getValue(String.class);

                    if(passwordFromDB.equals(inputPassword)){
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(inputPhoneNo).child("fullName").getValue(String.class);
                        String usernameFromDB = snapshot.child(inputPhoneNo).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(inputPhoneNo).child("email").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(inputPhoneNo).child("phoneNo").getValue(String.class);

                        userLocalStore.storeUserData(
                                new UserHelperClass(
                                        nameFromDB,
                                        usernameFromDB,
                                        emailFromDB,
                                        phoneNoFromDB,
                                        passwordFromDB
                                        )
                        );
                        userLocalStore.setUserLoggedIn(true);

                        Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);

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
                    phoneNumber.setError("No such user exists");
                    phoneNumber.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(isUserLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);

            UserHelperClass userLoggedIn = userLocalStore.getLoggedInUser();

            intent.putExtra("name", userLoggedIn.getFullName());
            intent.putExtra("username", userLoggedIn.getUsername());
            intent.putExtra("email", userLoggedIn.getEmail());
            intent.putExtra("phoneNo", userLoggedIn.getPhoneNo());
            intent.putExtra("password", userLoggedIn.getPassword());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean isUserLoggedIn(){
        return userLocalStore.getUserLoggedIn();
    }
}
