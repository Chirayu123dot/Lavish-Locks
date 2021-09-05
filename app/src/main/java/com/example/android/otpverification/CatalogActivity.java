package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CatalogActivity extends AppCompatActivity {

    Button profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));

        profile = findViewById(R.id.user_profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, UserProfile.class);

                Intent intent1 = getIntent();

                String username = intent.getStringExtra("username");
                String name = intent.getStringExtra("name");
                String email = intent.getStringExtra("email");
                String phoneNo = intent.getStringExtra("phoneNo");
                String password = intent.getStringExtra("password");

                intent.putExtra("name", name);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("password", password);

                startActivity(intent);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.catalog_menu, menu);
//        return true;
//    }
}
