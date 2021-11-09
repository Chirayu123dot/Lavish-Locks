package com.example.android.otpverification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private serviceAdapter adapter;
    private EditText searchView;
    private ImageView backButton;
    private ImageView userProfileButton;
    private ImageView searchButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    String name, phoneNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        searchView = findViewById(R.id.search_view);
        backButton = findViewById(R.id.user_profile_activity_back_button);
        userProfileButton = findViewById(R.id.catalog_activity_user_profile_button);
        searchButton = findViewById(R.id.catalog_activity_search_button);

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

//        searchView.setQueryHint(Html.fromHtml("<font color=\"#ffffff\">" + "<i>" + "Search" + "</i>" + "</font>"));
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setFocusable(true);
//            }
//        });

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

         name = getIntent().getStringExtra("name");
         phoneNumber = getIntent().getStringExtra("phoneNo");

        adapter = new serviceAdapter(options, name, phoneNumber, this);

        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = formatString(searchView.getText().toString());
                Log.v("CatalogActivity", searchText);
                firebaseUserSearch(searchText);
                searchView.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
        });
    }

    private void firebaseUserSearch(String searchText){
        Query firebaseSearchQuery = reference.orderByChild("Name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Service> options =
                new FirebaseRecyclerOptions.Builder<Service>()
                        .setQuery(firebaseSearchQuery, Service.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new serviceAdapter(options, name, phoneNumber, this);
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

    private String formatString(String serviceName){
        String formattedString = "";
        boolean isNewWord = true;
        for(int i=0 ; i<serviceName.length() ; i++){
            if(isNewWord){
                formattedString += Character.toUpperCase(serviceName.charAt(i));
                isNewWord = false;
            }else if(serviceName.charAt(i) == ' '){
                isNewWord = true;
            }else{
                formattedString += serviceName.charAt(i);
            }
        }
        return formattedString;
    }

}
