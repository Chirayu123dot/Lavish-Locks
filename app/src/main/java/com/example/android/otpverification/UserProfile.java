package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {

    EditText emailField, phoneNumberField, passwordField;
    TextView nameField;
    ImageView backButton;
    FloatingActionButton editButton;
    ImageView logOutButton;
    UserLocalStore userLocalStore;

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
        logOutButton = findViewById(R.id.user_profile_activity_logout_button);

        userLocalStore = new UserLocalStore(this);

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

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfile.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                userLocalStore.clearUserData();
                startActivity(intent);
            }
        });

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
