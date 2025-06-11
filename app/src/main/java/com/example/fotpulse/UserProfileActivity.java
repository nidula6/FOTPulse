package com.example.fotpulse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView logoutIcon, backIcon;
    private TextView profileNameTextView, profileTelephoneTextView, profileEmailTextView, profileBirthdayTextView, profileIndexTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

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
        profileTelephoneTextView = findViewById(R.id.profileTelephoneTextView);
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        profileBirthdayTextView = findViewById(R.id.profileBirthdayTextView);
        profileIndexTextView = findViewById(R.id.profileIndexTextView);
        logoutIcon = findViewById(R.id.logout_icon);  // Reference to the logout icon
        backIcon = findViewById(R.id.back_icon);  // Reference to the back icon

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

                        // Optional: Set a dummy phone number and date of birth, or fetch them if available
                        profileTelephoneTextView.setText("Telephone: +123456789");
                        profileBirthdayTextView.setText("Date Of Birth: 01/01/1990");
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });

            // Set OnClickListener for the logout icon to show confirmation dialog
            logoutIcon.setOnClickListener(v -> {
                // Create an AlertDialog to confirm sign out
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setMessage("Are you sure you want to log out?")
                        .setCancelable(false) // Prevent dismissing by tapping outside
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Log out the user
                                mAuth.signOut();

                                // Optionally show a toast to confirm the user is logged out
                                Toast.makeText(UserProfileActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

                                // Redirect to the login activity or home page (you can choose either)
                                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);  // Replace with your LoginActivity
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear previous activities
                                startActivity(intent);
                                finish();  // Close the UserProfileActivity
                            }
                        })
                        .setNegativeButton("No", null)  // Dismiss the dialog when "No" is clicked
                        .show();
            });

            // Set OnClickListener for the back icon
            backIcon.setOnClickListener(v -> {
                // Use onBackPressed to navigate back to the previous activity
                onBackPressed();
            });
        } else {
            // Handle the case where user is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to login activity if the user is not logged in
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
