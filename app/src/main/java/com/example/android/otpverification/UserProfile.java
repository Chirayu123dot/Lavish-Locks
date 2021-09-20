package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullNameField, emailField, phoneNoField, passwordField;
    FloatingActionButton editButton;
    TextView fullNameLabel, usernameLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getSupportActionBar().hide();


        fullNameField = findViewById(R.id.full_name_edit_text);
        emailField = findViewById(R.id.email_edit_text);
        phoneNoField = findViewById(R.id.phone_no_edit_text);
        passwordField = findViewById(R.id.password_edit_text);
        editButton = findViewById(R.id.edit_button);
        fullNameLabel = findViewById(R.id.full_name);
        usernameLabel = findViewById(R.id.username);

        fullNameField.setEnabled(false);
        emailField.setEnabled(false);
        phoneNoField.setEnabled(false);
        passwordField.setEnabled(false);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PasswordDialogFragment();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });

        showUserData();
    }

    private void showUserData(){
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phoneNo = intent.getStringExtra("phoneNo");
        String password = intent.getStringExtra("password");

        fullNameLabel.setText(name);
        usernameLabel.setText(username);
        fullNameField.getEditText().setText(name);
        emailField.getEditText().setText(email);
        phoneNoField.getEditText().setText(phoneNo);
        passwordField.getEditText().setText(password);
    }
}
