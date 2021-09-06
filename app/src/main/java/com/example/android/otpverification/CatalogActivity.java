package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
        getSupportActionBar().setElevation(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.user_profile) {
            showUserProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUserProfile(){
        Intent intent = new Intent(CatalogActivity.this, UserProfile.class);

        Intent intent1 = getIntent();

        String username = intent1.getStringExtra("username");
        String name = intent1.getStringExtra("name");
        String email = intent1.getStringExtra("email");
        String phoneNo = intent1.getStringExtra("phoneNo");
        String password = intent1.getStringExtra("password");

        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("password", password);

        startActivity(intent);
    }

}
