package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    FirebaseAuth auth;

    String TAG = "SignUpActivity";

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

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
        auth = FirebaseAuth.getInstance();

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
                    String mFullName = Objects.requireNonNull(name.getEditText()).getText().toString().trim();
                    String mUsername = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
                    String mEmail = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
                    String mPhoneNo = Objects.requireNonNull(phoneNo.getEditText()).getText().toString().trim();
                    String mPassword = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

                    // Send the user an OTP to verify their phone number
                    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    // This callback will be invoked in two situations:
                                    // 1 - Instant verification. In some cases the phone number can be instantly
                                    //     verified without needing to send or enter a verification code.
                                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                    //     detect the incoming verification SMS and perform verification without
                                    //     user action.
                                    Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                                    FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);

                                    signInWithPhoneAuthCredential(phoneAuthCredential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    // This callback is invoked in an invalid request for verification is made,
                                    // for instance if the the phone number format is not valid.
                                    Log.w(TAG, "onVerificationFailed", e);

                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        // Invalid request
                                        Log.e(TAG, e.getMessage());

                                    } else if (e instanceof FirebaseTooManyRequestsException) {
                                        // The SMS quota for the project has been exceeded
                                        Log.e(TAG, e.getMessage());
                                    }

                                    // Show a message and update the UI
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    // The SMS verification code has been sent to the provided phone number, we
                                    // now need to ask the user to enter the code and then construct a credential
                                    // by combining the code with a verification ID.
                                    Log.d(TAG, "onCodeSent:" + s);

                                    // Save verification ID and resending token so we can use them later
                                    mVerificationId = s;
                                    mResendToken = forceResendingToken;

                                    Intent intent = new Intent(SignUp.this, VerifyOTP.class);
                                    intent.putExtra("name", mFullName);
                                    intent.putExtra("username", mUsername);
                                    intent.putExtra("email", mEmail);
                                    intent.putExtra("phoneNo", mPhoneNo);
                                    intent.putExtra("password", mPassword);
                                    intent.putExtra("verificationCodeSent", s);
                                    startActivity(intent);
                                }
                            };

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber("+91" + mPhoneNo)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(SignUp.this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

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
            String specialCharacter = "[^\\w\\s]";
            String letter = "^[a-zA-Z]+$";
            if(val.length() < 4) password.setError("Password is too weak. Must be minimum 4 characters long");
            else if(!val.matches(letter)) password.setError("Password is too weak. Must contain a letter");
            else if(!val.matches(specialCharacter)) password.setError("Password is too weak. Must contain a special character");
            else password.setError("Password is too weak");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}
