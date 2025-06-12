package com.example.TechPulse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class UserProfileActivity extends AppCompatActivity {

    private ImageView logoutIcon, backIcon;
    private TextView profileNameTextView, profileEmailTextView, profileTelephoneTextView, profileBirthdayTextView, profileIndexTextView;
    private EditText profileNameEditText, profileEmailEditText, profileTelephoneEditText, profileBirthdayEditText, profileIndexEditText;
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
        profileTelephoneTextView = findViewById(R.id.profileTelephoneTextView);
        profileBirthdayTextView = findViewById(R.id.profileBirthdayTextView);
        profileIndexTextView = findViewById(R.id.profileIndexTextView);
        profileNameEditText = findViewById(R.id.profileNameEditText);
        profileEmailEditText = findViewById(R.id.profileEmailEditText);
        profileTelephoneEditText = findViewById(R.id.profileTelephoneEditText);
        profileBirthdayEditText = findViewById(R.id.profileBirthdayEditText);
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
                        String telephone = dataSnapshot.child("telephone").getValue(String.class);
                        String birthday = dataSnapshot.child("birthday").getValue(String.class);
                        String index = dataSnapshot.child("indexNumber").getValue(String.class);

                        // Set data to TextViews
                        profileNameTextView.setText("Name: " + name);
                        profileEmailTextView.setText("Email: " + email);
                        profileTelephoneTextView.setText("Telephone: " + telephone);
                        profileBirthdayTextView.setText("Date of Birth: " + birthday);
                        profileIndexTextView.setText("Index: " + index);
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });

            // Edit details logic
            editDetailsButton.setOnClickListener(v -> {
                // Check if any of the EditText views are currently visible
                boolean isEditing = profileNameEditText.getVisibility() == View.VISIBLE;

                if (isEditing) {
                    // Save changes to Firebase for editable fields
                    String updatedName = profileNameEditText.getText().toString();
                    String updatedTelephone = profileTelephoneEditText.getText().toString();
                    String updatedBirthday = profileBirthdayEditText.getText().toString();
                    String updatedIndex = profileIndexEditText.getText().toString();

                    // Do not update the email, keep it the same
                    usersRef.child(userId).child("name").setValue(updatedName);
                    usersRef.child(userId).child("telephone").setValue(updatedTelephone);
                    usersRef.child(userId).child("birthday").setValue(updatedBirthday);
                    usersRef.child(userId).child("indexNumber").setValue(updatedIndex);

                    // Update the TextViews with the new values
                    profileNameTextView.setText("Name: " + updatedName);
                    profileTelephoneTextView.setText("Telephone: " + updatedTelephone);
                    profileBirthdayTextView.setText("Date of Birth: " + updatedBirthday);
                    profileIndexTextView.setText("Index: " + updatedIndex);
                }

                // Toggle visibility for editable fields
                profileNameEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);
                profileTelephoneEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);
                profileBirthdayEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);
                profileIndexEditText.setVisibility(isEditing ? View.GONE : View.VISIBLE);

                // Keep email TextView always visible
                profileEmailTextView.setVisibility(View.VISIBLE);

                // Toggle visibility for non-editable TextViews
                profileNameTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);
                profileTelephoneTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);
                profileBirthdayTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);
                profileIndexTextView.setVisibility(isEditing ? View.VISIBLE : View.GONE);

                // Change button text based on editing state
                editDetailsButton.setText(isEditing ? "Edit User" : "Save Changes");
            });

        }

        // Handle logout and back icons
        logoutIcon.setOnClickListener(v -> {
            // Show a confirmation dialog
            new AlertDialog.Builder(UserProfileActivity.this)
                    .setMessage("Are you sure you want to log out?")
                    .setCancelable(false)  // Prevent dismissing by tapping outside
                    .setPositiveButton("Yes", (dialog, id) -> {
                        mAuth.signOut();  // Perform logout
                        Toast.makeText(UserProfileActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                        finish();  // Close the UserProfileActivity
                    })
                    .setNegativeButton("No", null)  // Dismiss the dialog when "No" is clicked
                    .show();  // Show the dialog
        });


        backIcon.setOnClickListener(v -> onBackPressed());

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
    }


}

