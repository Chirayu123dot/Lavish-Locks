package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    TextInputLayout fullName;
//    TextInputLayout username;
    TextInputLayout email;
    TextInputLayout phoneNumber;
    TextInputLayout password;
    TextInputEditText fullNameEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneNumberEditText;
    TextInputEditText passwordEditText;
    Button signupButton;
    TextView loginButton;
    ImageView backButton;
    ProgressBar progressBar;

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

        fullName = findViewById(R.id.signup_activity_fullname_text_input_layout);
//        username = findViewById(R.id.phone_number);
        email = findViewById(R.id.signup_activity_email_text_input_layout);
        phoneNumber = findViewById(R.id.signup_activity_phone_no_text_input_layout);
        password = findViewById(R.id.signup_activity_password_text_input_layout);
        signupButton = findViewById(R.id.signup_activity_signup_button);
        loginButton = findViewById(R.id.signup_activity_login_button);
        fullNameEditText = findViewById(R.id.signup_activity_fullname_edit_text);
        emailEditText = findViewById(R.id.signup_activity_email_edit_text);
        phoneNumberEditText = findViewById(R.id.signup_activity_phone_no_edit_text);
        passwordEditText = findViewById(R.id.signup_activity_password_edit_text);
        backButton = findViewById(R.id.user_profile_activity_back_button);
        progressBar = findViewById(R.id.signup_activity_progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateName()  & validateEmail() & validatePhoneNo() & validatePassword2()){
                    String mFullName = fullName.getEditText().getText().toString().trim();
                    String mEmail = email.getEditText().getText().toString().trim();
                    String mPhoneNo = phoneNumber.getEditText().getText().toString().trim();
                    String mPassword = password.getEditText().getText().toString().trim();

                    signupButton.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    Log.v("SignUp", "HELOOOO!!! sIGN UP");
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + mPhoneNo,
                            60,
                            TimeUnit.SECONDS,
                            SignUp.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    Toast.makeText(SignUp.this, "onVerificationCompleted", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                                    intent.putExtra("name", mFullName);
                                    intent.putExtra("username", "no username");
                                    intent.putExtra("email", mEmail);
                                    intent.putExtra("phoneNo", mPhoneNo);
                                    intent.putExtra("password", mPassword);
                                    intent.putExtra("verificationCodeSent", s);
                                    startActivity(intent);
                                }
                            }
                    );
//                    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
//                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                @Override
//                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                    // This callback will be invoked in two situations:
//                                    // 1 - Instant verification. In some cases the phone number can be instantly
//                                    //     verified without needing to send or enter a verification code.
//                                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                                    //     detect the incoming verification SMS and perform verification without
//                                    //     user action.
//                                    Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
//
//                                    // Turn off phone auth app verification.
////                                    FirebaseAuth.getInstance().getFirebaseAuthSettings()
////                                            .setAppVerificationDisabledForTesting(false);
//
//                                    FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);
//
//                                    signInWithPhoneAuthCredential(phoneAuthCredential);
//                                }
//
//                                @Override
//                                public void onVerificationFailed(@NonNull FirebaseException e) {
//                                    // This callback is invoked in an invalid request for verification is made,
//                                    // for instance if the the phone number format is not valid.
//                                    Log.w(TAG, "onVerificationFailed", e);
//
//                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                                        // Invalid request
//                                        Log.e(TAG, e.getMessage());
//
//                                    } else if (e instanceof FirebaseTooManyRequestsException) {
//                                        // The SMS quota for the project has been exceeded
//                                        Log.e(TAG, e.getMessage());
//                                    }
//
//                                    // Show a message and update the UI
//                                }
//
//                                @Override
//                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                    // The SMS verification code has been sent to the provided phone number, we
//                                    // now need to ask the user to enter the code and then construct a credential
//                                    // by combining the code with a verification ID.
//                                    Log.d(TAG, "onCodeSent:" + s);
//
//                                    // Save verification ID and resending token so we can use them later
//                                    mVerificationId = s;
//                                    mResendToken = forceResendingToken;
//
//                                    Intent intent = new Intent(SignUp.this, VerifyOTP.class);
//                                    intent.putExtra("name", mFullName);
//                                    intent.putExtra("username", mUsername);
//                                    intent.putExtra("email", mEmail);
//                                    intent.putExtra("phoneNo", mPhoneNo);
//                                    intent.putExtra("password", mPassword);
//                                    intent.putExtra("verificationCodeSent", s);
//                                    startActivity(intent);
//                                }
//                            };
//
//                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
//                            .setPhoneNumber("+91" + mPhoneNo)       // Phone number to verify
//                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                            .setActivity(SignUp.this)                 // Activity (for callback binding)
//                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                            .build();
//
//                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        designEditTextFields();
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
        if(TextUtils.isEmpty(fullName.getEditText().getText().toString().trim())){
            fullName.setError("Field cannot be empty");
            return false;
        }
        else{
            fullName.setError(null);
            fullName.setErrorEnabled(false);
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
//
//    boolean validatePassword(){
//
//        String val = password.getEditText().getText().toString().trim();
//        String passwordPattern = "^" +
//                //"(?=.*[0-9])" +         //at least 1 digit
//                //"(?=.*[a-z])" +         //at least 1 lower case letter
//                //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=\\S+$)" +           //no white spaces
//                ".{4,}" +               //at least 4 characters
//                "$";
//
//        if(val.isEmpty()){
//            password.setError("Field cannot be empty");
//            return false;
//        }
//        else if(!val.matches(passwordPattern)){
//            String specialCharacter = "[^\\w\\s]";
//            String letter = "^[a-zA-Z]+$";
//            if(val.length() < 4) password.setError("Password is too weak. Must be minimum 4 characters long");
//            else if(!val.matches(letter)) password.setError("Password is too weak. Must contain a letter");
//            else if(!val.matches(specialCharacter)) password.setError("Password is too weak. Must contain a special character");
//            else password.setError("Password is too weak");
//            return false;
//        }
//        else{
//            password.setError(null);
//            password.setErrorEnabled(false);
//            return true;
//        }
//    }

    boolean validatePassword2(){
        String val = password.getEditText().getText().toString().trim();

//        String specialCharacterRegex = "(?=.*[^A-Za-z0-9])";
//        String alphabetRegex = "(?=.*[a-z])";

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else if(val.length() < 6){
            password.setError("Password is too weak. Must be minimum 6 characters long");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
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

        fullName.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Full Name" + "</i>" + "</font>"));
        fullName.setEndIconVisible(false);

        fullNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    fullName.setEndIconVisible(true);
                    fullName.setHint(" ");
                    fullName.setHint("Full Name");
                }
                else{
                    fullName.setEndIconVisible(false);
                    if(TextUtils.isEmpty(fullName.getEditText().getText().toString().trim())){
                        fullName.setHint(" ");
                        fullName.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Full Name" + "</i>" + "</font>"));
                    }else {
                        fullName.setHint(" ");
                        fullName.setHint("Full Name");
                    }
                }
            }
        });

        email.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Email" + "</i>" + "</font>"));
        email.setEndIconVisible(false);

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    email.setEndIconVisible(true);
                    email.setHint(" ");
                    email.setHint("Email");
                }
                else{
                    email.setEndIconVisible(false);
                    if(TextUtils.isEmpty(email.getEditText().getText().toString().trim())){
                        email.setHint(" ");
                        email.setHint(Html.fromHtml("<font color=\"#BDBDBD\">" + "<i>" + "Email" + "</i>" + "</font>"));
                    }else {
                        email.setHint(" ");
                        email.setHint("Email");
                    }
                }
            }
        });

    }
}
