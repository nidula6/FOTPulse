package com.example.fotpulse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        // Inside DeveloperActivity.java
        ImageView backIcon = findViewById(R.id.back_icon);

// Set OnClickListener for the back icon
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use onBackPressed to navigate back to the previous activity
                onBackPressed();
            }
        });

        // Ensure you have activity_developer.xml
    }
}
