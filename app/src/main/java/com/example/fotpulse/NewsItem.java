package com.example.fotpulse;

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
        return timestamp;
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
