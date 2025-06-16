package com.example.TechPulse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.TechPulse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private ImageView logoutIcon;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList;
    private List<NewsItem> filteredNewsList;  // Filtered news list for search
    private DatabaseReference newsRef;
    private ImageView highlightImage, profileImage;

    private FirebaseAuth mAuth;  // Declare FirebaseAuth instance
    private String imageUrl, newsTitle, newsDescription, newsType, newsTimestamp;

    // Declare ImageView variables
    ImageView tabSports, tabAcademics, tabEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabSports = findViewById(R.id.tab_sports);
        tabAcademics = findViewById(R.id.tab_academics);
        tabEvents = findViewById(R.id.tab_fot_events); // Make sure you have this ID defined


        // Set onClickListener for Sports tab

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();  // Initialize mAuth

        // Bind views
        logoutIcon = findViewById(R.id.logout_icon);
        profileImage = findViewById(R.id.profileImageView);  // Profile image for navigation
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));  // Set LayoutManager
        highlightImage = findViewById(R.id.highlight_image);

        // Initialize Firebase Realtime Database reference
        newsRef = FirebaseDatabase.getInstance().getReference("news");

        // Initialize lists and adapter
        newsList = new ArrayList<>();
        filteredNewsList = new ArrayList<>();  // Initialize the filtered list
        newsAdapter = new NewsAdapter(filteredNewsList, this);  // Use filtered list in the adapter
        newsRecyclerView.setAdapter(newsAdapter);  // Set Adapter to RecyclerView

        // Fetch news items from Firebase
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list before adding new data
                newsList.clear();

                // Loop through the data and add it to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewsItem newsItem = snapshot.getValue(NewsItem.class);

                    if (newsItem != null) {
                        newsList.add(newsItem);

                        // Set the first news item or a specific news item as the highlight image
                        if (snapshot.getKey().equals("8")) {
                            imageUrl = newsItem.getImageUrl();
                            newsTitle = newsItem.getTitle();
                            newsDescription = newsItem.getDescription();
                            newsType = newsItem.getType();

                            // Convert timestamp (long) to formatted String
                            long timestampLong = newsItem.getTimestamp();  // Assuming it's a long in NewsItem
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            newsTimestamp = sdf.format(new Date(timestampLong));  // Convert to String

                            // Load the image into the ImageView using Glide
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(HomeActivity.this).load(imageUrl).into(highlightImage);
                            } else {
                                highlightImage.setImageResource(R.drawable.breakingnews);
                            }
                        }
                    }
                }

                // Also update the filtered list
                filteredNewsList.clear();
                filteredNewsList.addAll(newsList);
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors during database access
                Toast.makeText(HomeActivity.this, "Failed to load news: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Search functionality
        EditText searchBar = findViewById(R.id.search_bar);  // Reference to the EditText search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter the news based on search text
                String query = charSequence.toString().toLowerCase();  // Convert to lowercase for case-insensitive search
                filteredNewsList.clear();

                // Add matching news items to filtered list
                for (NewsItem newsItem : newsList) {
                    if (newsItem.getTitle().toLowerCase().contains(query) ||
                            newsItem.getDescription().toLowerCase().contains(query)) {
                        filteredNewsList.add(newsItem);
                    }
                }

                // Notify the adapter of the filtered data
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Optional: Handle after text changes
            }
        });

        // Set OnClickListener for the highlight image (same as before)
        highlightImage.setOnClickListener(v -> {
            if (newsTitle != null && newsDescription != null) {
                Intent intent = new Intent(HomeActivity.this, FullNewsActivity.class);
                intent.putExtra("newsTitle", newsTitle);
                intent.putExtra("newsDescription", newsDescription);
                intent.putExtra("newsImage", imageUrl);
                intent.putExtra("newsType", newsType);
                intent.putExtra("newsTimestamp", newsTimestamp);
                startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this, "No image or data to display", Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnClickListener for the profile image (navigate to UserProfileActivity)
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(intent);  // Navigate to UserProfileActivity
        });

        // Set OnClickListener for the logout icon to show confirmation dialog
        logoutIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(HomeActivity.this)
                    .setMessage("Are you sure you want to log out?")
                    .setCancelable(false) // Prevent dismissing by tapping outside
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Log out the user
                            mAuth.signOut();

                            // Optionally show a toast to confirm the user is logged out
                            Toast.makeText(HomeActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

                            // Redirect to the login activity
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);  // Replace with your LoginActivity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear previous activities
                            startActivity(intent);
                            finish();  // Close the HomeActivity
                        }
                    })
                    .setNegativeButton("No", null)  // Dismiss the dialog when "No" is clicked
                    .show();  // Show the dialog


        });
        // Set OnClickListener for the Sports tab to navigate to CategoryNewsActivity
        tabSports.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryNewsActivity.class);
            intent.putExtra("category", "Sports");  // Pass category as extra
            startActivity(intent);  // Start CategoryNewsActivity
        });

// Set OnClickListener for the Academics tab to navigate to CategoryNewsActivity
        tabAcademics.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryNewsActivity.class);
            intent.putExtra("category", "Academics");  // Pass category as extra
            startActivity(intent);  // Start CategoryNewsActivity
        });

// Set OnClickListener for the Events tab to navigate to CategoryNewsActivity
        tabEvents.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryNewsActivity.class);
            intent.putExtra("category", "Events");  // Pass category as extra
            startActivity(intent);  // Start CategoryNewsActivity
        });

    }


}
