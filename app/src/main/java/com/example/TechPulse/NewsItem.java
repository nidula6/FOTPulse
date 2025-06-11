package com.example.TechPulse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsItem {

    private String type;
    private String title;
    private String description;
    private long timestamp;
    private String imageUrl;

    // No-argument constructor required by Firebase
    public NewsItem() {
    }

    // Constructor with parameters (optional, for manual creation of objects)
    public NewsItem(String type, String title, String description, long timestamp, String imageUrl) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public String getTimestampString() {
        // Convert long timestamp to a readable String format
        Date date = new Date(timestamp); // Correct constructor for long timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date); // Format the date into a string
    }

    // Getters and Setters for Firebase to access the data
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp; // Return as long
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
