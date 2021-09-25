package com.example.android.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private serviceAdapter adapter;
    private SearchView searchView;
    private ImageView backButton;
    private ImageView userProfileButton;

    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        searchView = findViewById(R.id.search_view);
        backButton = findViewById(R.id.user_profile_activity_back_button);
        userProfileButton = findViewById(R.id.catalog_activity_user_profile_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserProfile();
            }
        });

//        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
//        searchIcon.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_search_icon));
        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.white));

        searchView.setQueryHint(Html.fromHtml("<font color=\"#ffffff\">" + "<i>" + "Search" + "</i>" + "</font>"));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setFocusable(true);
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Services");

        recyclerView = findViewById(R.id.catalog_activity_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Service> options
                = new FirebaseRecyclerOptions.Builder<Service>()
                .setQuery(reference, Service.class)
                .build();

        adapter = new serviceAdapter(options);

        recyclerView.setAdapter(adapter);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.catalog_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.user_profile) {
//            showUserProfile();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
