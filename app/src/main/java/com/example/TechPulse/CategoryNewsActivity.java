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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryNewsActivity extends AppCompatActivity {

    private LinearLayout newsContainer;  // The container where we will dynamically add news items
    private List<NewsItem> newsList;  // List to hold news items
    private DatabaseReference newsRef;
    private ImageView backIcon2;  // Declare the back icon


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news);  // Ensure this layout contains the newsContainer

        newsContainer = findViewById(R.id.newsContainer);  // Get the LinearLayout container
        backIcon2 = findViewById(R.id.back_icon2);
        // Initialize news list
        newsList = new ArrayList<>();

        // Initialize Firebase Database reference
        newsRef = FirebaseDatabase.getInstance().getReference("news");

        // Fetch the category passed from the previous activity
        String category = getIntent().getStringExtra("category");
        backIcon2.setOnClickListener(v -> {
            // Create an Intent to navigate back to HomeActivity
            Intent intent = new Intent(CategoryNewsActivity.this, HomeActivity.class);
            startActivity(intent);  // Start HomeActivity
            finish();  // Optional: Close CategoryNewsActivity if you don't want it in the back stack
        });
        // Fetch data from Firebase and filter by category
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();  // Clear previous data in the list
                newsContainer.removeAllViews();  // Remove all views from the container before adding new ones

                // Loop through the data and add to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewsItem newsItem = snapshot.getValue(NewsItem.class);

                    // Filter the news by category
                    if (newsItem != null && newsItem.getType().equalsIgnoreCase(category)) {
                        newsList.add(newsItem);  // Add the news item to the list if the category matches

                        // Dynamically create a news item layout (CardView with TextViews)
                        View newsView = LayoutInflater.from(CategoryNewsActivity.this).inflate(R.layout.news_item, null);

                        // Bind the views
                        TextView newsType = newsView.findViewById(R.id.newsType);
                        TextView newsTitle = newsView.findViewById(R.id.newsTitle);
                        TextView newsDescription = newsView.findViewById(R.id.newsDescription);
                        TextView newsTimestamp = newsView.findViewById(R.id.newsTimestamp);
                        ImageView newsImage = newsView.findViewById(R.id.newsImage);

                        // Set the data for the current news item
                        newsType.setText(newsItem.getType());
                        newsTitle.setText(newsItem.getTitle());
                        newsDescription.setText(newsItem.getDescription());
                        newsTimestamp.setText(newsItem.getTimestampString());

                        // Use Glide to load the image if it exists
                        if (newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty()) {
                            Glide.with(CategoryNewsActivity.this).load(newsItem.getImageUrl()).into(newsImage);
                        } else {
                            // Set a placeholder or default image if the URL is empty
                            newsImage.setImageResource(R.drawable.breakingnews);
                        }

                        // Add the news item view to the container
                        newsContainer.addView(newsView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors during database access
                Toast.makeText(CategoryNewsActivity.this, "Failed to load news: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
