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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView serviceRecyclerView;
    private serviceAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Services");

        serviceRecyclerView = findViewById(R.id.services_recycler_view);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Service> options
                = new FirebaseRecyclerOptions.Builder<Service>()
                .setQuery(reference, Service.class)
                .build();

        adapter = new serviceAdapter(options);

        serviceRecyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
        getSupportActionBar().setElevation(0);

    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
