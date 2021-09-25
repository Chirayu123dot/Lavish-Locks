package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserProfile extends AppCompatActivity {

    EditText emailField, phoneNumberField, passwordField;
    TextView nameField;
    ImageView backButton;
    FloatingActionButton editButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);


        nameField = findViewById(R.id.user_profile_activity_name_field);
        emailField = findViewById(R.id.user_profile_activity_email_field);
        phoneNumberField = findViewById(R.id.user_profile_activity_phone_number_field);
        passwordField = findViewById(R.id.user_profile_activity_password_field);
        backButton = findViewById(R.id.user_profile_activity_back_button);
//        editButton = findViewById(R.id.edit_button);

        emailField.setEnabled(false);
        phoneNumberField.setEnabled(false);
        passwordField.setEnabled(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new PasswordDialogFragment();
//                newFragment.show(getSupportFragmentManager(), "missiles");
//            }
//        });

        showUserData();
    }

    private void showUserData(){
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phoneNumber = intent.getStringExtra("phoneNo");
        String password = intent.getStringExtra("password");

        nameField.setText(name);
        emailField.setText(email);
        phoneNumberField.setText(phoneNumber);
        passwordField.setText(password);
    }
}
