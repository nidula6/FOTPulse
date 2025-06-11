package com.example.fotpulse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView logoutIcon, backIcon;
    private TextView profileNameTextView, profileEmailTextView, profileIndexTextView;
    private EditText profileNameEditText, profileEmailEditText, profileIndexEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private MaterialButton editDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase Authentication and Database
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Bind views
        profileNameTextView = findViewById(R.id.profileNameTextView);
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        profileIndexTextView = findViewById(R.id.profileIndexTextView);
        profileNameEditText = findViewById(R.id.profileNameEditText);
        profileEmailEditText = findViewById(R.id.profileEmailEditText);
        profileIndexEditText = findViewById(R.id.profileIndexEditText);
        logoutIcon = findViewById(R.id.logout_icon);
        backIcon = findViewById(R.id.back_icon);
        editDetailsButton = findViewById(R.id.EditDetails);

        // Fetch data for current user
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get current user's ID

            // Get user data from Firebase Realtime Database
            usersRef.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String index = dataSnapshot.child("indexNumber").getValue(String.class);

                        // Set data to TextViews
                        profileNameTextView.setText("Name: " + name);
                        profileEmailTextView.setText("Email: " + email);
                        profileIndexTextView.setText("Index: " + index);
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });

            // Edit details logic
            editDetailsButton.setOnClickListener(v -> {
                // Switch between EditText and TextView for editing user details
                boolean isEditing = profileNameEditText.getVisibility() == View.VISIBLE;

                if (isEditing) {
                    // Save changes to Firebase
                    String updatedName = profileNameEditText.getText().toString();
                    String updatedEmail = profileEmailEditText.getText().toString();
                    String updatedIndex = profileIndexEditText.getText().toString();

                    usersRef.child(userId).child("name").setValue(updatedName);
                    usersRef.child(userId).child("email").setValue(updatedEmail);
                    usersRef.child(userId).child("indexNumber").setValue(updatedIndex);

                    // Update the TextViews
                    profileNameTextView.setText("Name: " + updatedName);
                    profileEmailTextView.setText("Email: " + updatedEmail);
                    profileIndexTextView.setText("Index: " + updatedIndex);
                }

                // Toggle visibility
                profileNameEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);
                profileEmailEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);
                profileIndexEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);

                profileNameTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);
                profileEmailTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);
                profileIndexTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);

                // Update button text
                editDetailsButton.setText(isEditing ? "Edit User" : "Save Changes");
            });
        }
        // Inside UserProfileActivity.java
        TextView developerInfoTextView = findViewById(R.id.DeveloperInfo);

// Set OnClickListener for the TextView
        developerInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start DeveloperActivity when the TextView is clicked
                Intent intent = new Intent(UserProfileActivity.this, DeveloperActivity.class);
                startActivity(intent);
            }
        });


        // Handle the logout icon click
        logoutIcon.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Handle the back icon click
        backIcon.setOnClickListener(v -> onBackPressed());
    }


}
