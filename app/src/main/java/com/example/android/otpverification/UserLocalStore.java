package com.example.android.otpverification;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);

    }

    public void storeUserData(UserHelperClass user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("phoneNumber", user.getPhoneNo());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("fullName", user.getFullName());
        spEditor.putString("email", user.getEmail());
        spEditor.commit();
    }

    public UserHelperClass getLoggedInUser(){
        String phoneNumber = userLocalDatabase.getString("phoneNumber", "");
        String password = userLocalDatabase.getString("password", "");
        String fullName = userLocalDatabase.getString("fullName", "");
        String email = userLocalDatabase.getString("email", "");

        UserHelperClass user = new UserHelperClass(fullName, "", email, phoneNumber, password);
        return user;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        }else{
            return false;
        }
    }
}
