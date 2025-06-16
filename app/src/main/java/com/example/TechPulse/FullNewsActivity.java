package com.example.TechPulse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullNewsActivity extends AppCompatActivity {

    private TextView newsTitle, newsDescription, newsType, newsTimestamp;
    private ImageView newsImage, backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);  // Ensure this is the correct layout

        // Initialize views
        newsTitle = findViewById(R.id.newsTitle);
        newsDescription = findViewById(R.id.newsDescription);
        newsType = findViewById(R.id.newsType);  // News type TextView
        newsTimestamp = findViewById(R.id.newsTimestamp);  // Timestamp TextView
        newsImage = findViewById(R.id.newsImage);
        backIcon = findViewById(R.id.back_icon);  // Back button ImageView

        // Retrieve data passed through the intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("newsTitle");
        String description = intent.getStringExtra("newsDescription");
        String imageUrl = intent.getStringExtra("newsImage");
        String type = intent.getStringExtra("newsType");  // Retrieve news type
        String timestamp = intent.getStringExtra("newsTimestamp");  // Retrieve timestamp

        // Set the data to the views
        newsTitle.setText(title);
        newsDescription.setText(description);
        newsType.setText(type);  // Set the news type to the TextView
        newsTimestamp.setText(timestamp);  // Set the timestamp to the TextView

        // Load the image using Glide
        Glide.with(this).load(imageUrl).into(newsImage);

        // Set up the back button click listener
        backIcon.setOnClickListener(v -> {

            finish();
        });
    }
}
