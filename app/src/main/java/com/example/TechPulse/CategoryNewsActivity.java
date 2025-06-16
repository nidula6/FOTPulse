package com.example.TechPulse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.TechPulse.NewsAdapter;
import com.example.TechPulse.NewsItem;
import com.example.TechPulse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryNewsActivity extends AppCompatActivity {
    private ImageView backIcon2;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList;
    private DatabaseReference newsRef;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news);


        // Initialize views
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));  // Set the layout manager to LinearLayoutManager
        backIcon2 = findViewById(R.id.backIcon2);

        // Get category passed from previous activity
        category = getIntent().getStringExtra("category");

        // Initialize news list and Firebase reference
        newsList = new ArrayList<>();
        newsRef = FirebaseDatabase.getInstance().getReference("news");

        // Fetch news from Firebase
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear(); // Clear the previous data

                // Loop through the data and add it to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewsItem newsItem = snapshot.getValue(NewsItem.class);

                    // Add news item if it matches the category
                    if (newsItem != null && newsItem.getType().equalsIgnoreCase(category)) {
                        newsList.add(newsItem);
                    }
                }

                // Set adapter
                newsAdapter = new NewsAdapter(newsList, CategoryNewsActivity.this);
                newsRecyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
        backIcon2.setOnClickListener(v -> {
            // Create an Intent to navigate back to HomeActivity
            Intent intent = new Intent(CategoryNewsActivity.this, HomeActivity.class);
            startActivity(intent);  // Start HomeActivity
            finish();
        });
    }
}
