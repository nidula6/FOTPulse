package com.example.fotpulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fotpulse.NewsItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList;
    private List<NewsItem> filteredNewsList;  // Filtered news list for search
    private DatabaseReference newsRef;
    private ImageView highlightImage;

    private String imageUrl, newsTitle, newsDescription, newsType, newsTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // Ensure this is the correct layout

        // Initialize RecyclerView and adapter
        newsRecyclerView = findViewById(R.id.newsRecyclerView);  // Ensure the ID matches the XML layout
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));  // Set LayoutManager
        highlightImage = findViewById(R.id.highlight_image);
        newsList = new ArrayList<>();
        filteredNewsList = new ArrayList<>();  // Initialize the filtered list
        newsAdapter = new NewsAdapter(filteredNewsList, this);  // Use filtered list in the adapter
        newsRecyclerView.setAdapter(newsAdapter);  // Set Adapter to RecyclerView

        // Initialize Firebase Realtime Database reference
        newsRef = FirebaseDatabase.getInstance().getReference("news");

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
                        if (snapshot.getKey().equals("8")) {  // Example condition to get highlight item (adjust as needed)
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
                                highlightImage.setImageResource(R.drawable.breakingnews);  // Use default image if URL is null
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
                // Optional: Handle before text changes
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
    }
}
