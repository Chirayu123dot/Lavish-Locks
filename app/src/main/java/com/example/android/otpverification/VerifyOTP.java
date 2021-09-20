package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyOTP extends AppCompatActivity {

    EditText otpField;
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

        otpField = findViewById(R.id.otp_field);
        verifyButton = findViewById(R.id.verify_button);

        verificationCodeSent = getIntent().getStringExtra("verificationCodeSent");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeEnteredByUser = otpField.getText().toString().trim();

                if(verificationCodeSent != null){
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationCodeSent, codeEnteredByUser
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
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
                                    }else{
                                        Toast.makeText(VerifyOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(VerifyOTP.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
