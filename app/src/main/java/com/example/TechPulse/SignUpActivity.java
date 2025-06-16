package com.example.TechPulse;

import android.content.Intent; // Import this
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText nameInput, emailInput, indexNumberInput, passwordInput, repeatPasswordInput;
    private TextInputLayout nameInputLayout, emailInputLayout, indexNumberInputLayout, passwordInputLayout, repeatPasswordInputLayout;
    private MaterialButton signUpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);  // Replace with your XML layout file

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize the views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        indexNumberInput = findViewById(R.id.indexNumberInput);
        passwordInput = findViewById(R.id.passwordInput);
        repeatPasswordInput = findViewById(R.id.RepeatpasswordInput);

        // Initialize TextInputLayout views
        nameInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        indexNumberInputLayout = findViewById(R.id.indexNumberInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        repeatPasswordInputLayout = findViewById(R.id.repeatPasswordInputLayout);  // Ensure the correct ID is used

        signUpButton = findViewById(R.id.signUpButton);

        // Set click listener for Sign Up button
        signUpButton.setOnClickListener(v -> {
            // Get input values
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String indexNumber = indexNumberInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String repeatPassword = repeatPasswordInput.getText().toString().trim();  // Declare repeatPassword inside the listener

            // Validate inputs
            if (TextUtils.isEmpty(name)) {
                nameInputLayout.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailInputLayout.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(indexNumber)) {
                indexNumberInputLayout.setError("Index number is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordInputLayout.setError("Password is required");
                return;
            }
            if (TextUtils.isEmpty(repeatPassword)) {
                repeatPasswordInputLayout.setError("Please repeat your password");
                return;
            }

            // Check if passwords match
            if (!password.equals(repeatPassword)) {
                repeatPasswordInputLayout.setError("Passwords do not match");
                return;
            }

            // Call Firebase sign-up method
            signUpUser(email, password, name, indexNumber);
        });

        // Set click listener for Sign In link to navigate to LoginActivity
        findViewById(R.id.signInLink).setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);  // Start the LoginActivity
            finish();  // Optional: Close SignUpActivity after navigating to LoginActivity
        });
    }

    // Firebase sign-up and save user data to Realtime Database
    private void signUpUser(String email, String password, String name, String indexNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid(); // Get user ID

                        // Initialize Firebase Realtime Database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference usersRef = database.getReference("users");

                        // Create a User object
                        User newUser = new User(name, email, indexNumber);

                        // Save user data to the database
                        usersRef.child(userId).setValue(newUser)
                                .addOnCompleteListener(databaseTask -> {
                                    if (databaseTask.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Registration and data saved!", Toast.LENGTH_SHORT).show();
                                        // Optionally, you can navigate to the next screen or finish this activity
                                        finish();
                                    } else {
                                        // Handle database write failure
                                        Toast.makeText(SignUpActivity.this, "Failed to save user data: " + databaseTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        // Registration failed
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // User model class for storing user data in Firebase Realtime Database
    public static class User {
        private String name;
        private String email;
        private String indexNumber;

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        public User() {}

        public User(String name, String email, String indexNumber) {
            this.name = name;
            this.email = email;
            this.indexNumber = indexNumber;
        }

        // Getters and setters (optional)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIndexNumber() {
            return indexNumber;
        }

        public void setIndexNumber(String indexNumber) {
            this.indexNumber = indexNumber;
        }
    }
}
