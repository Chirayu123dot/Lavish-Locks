package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyOTP extends AppCompatActivity {

    EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    Button verifyButton;
    String verificationCodeSent;
    String TAG = "VerifyOTP";


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        inputCode1 = findViewById(R.id.verify_otp_activity_input_char1);
        inputCode2 = findViewById(R.id.verify_otp_activity_input_char2);
        inputCode3 = findViewById(R.id.verify_otp_activity_input_char3);
        inputCode4 = findViewById(R.id.verify_otp_activity_input_char4);
        inputCode5 = findViewById(R.id.verify_otp_activity_input_char5);
        inputCode6 = findViewById(R.id.verify_otp_activity_input_char6);

        verifyButton = findViewById(R.id.verify_otp_activity_verify_button);

        verificationCodeSent = getIntent().getStringExtra("verificationCodeSent");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeEnteredByUser = inputCode1.getText().toString() + inputCode2.getText().toString()
                        + inputCode3.getText().toString() + inputCode4.getText().toString() + inputCode5.getText().toString()
                        + inputCode6.getText().toString();

                if(codeEnteredByUser.length() < 6){
                    Toast.makeText(VerifyOTP.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }else {
                    if (verificationCodeSent != null) {
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                verificationCodeSent, codeEnteredByUser
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(VerifyOTP.this, CatalogActivity.class);
                                            intent.putExtra("name", getIntent().getStringExtra("name"));
                                            intent.putExtra("username", getIntent().getStringExtra("username"));
                                            intent.putExtra("email", getIntent().getStringExtra("email"));
                                            intent.putExtra("phoneNo", getIntent().getStringExtra("phoneNo"));
                                            intent.putExtra("password", getIntent().getStringExtra("password"));

                                            UserHelperClass user = new UserHelperClass(
                                                    getIntent().getStringExtra("name"),
                                                    getIntent().getStringExtra("username"),
                                                    getIntent().getStringExtra("email"),
                                                    getIntent().getStringExtra("phoneNo"),
                                                    getIntent().getStringExtra("password"));

                                            reference.child(getIntent().getStringExtra("phoneNo")).setValue(user);

                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(VerifyOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(VerifyOTP.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        inputCodeMovement();
    }

    private void inputCodeMovement(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    inputCode2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    inputCode3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    inputCode4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    inputCode5.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    inputCode6.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }


}
