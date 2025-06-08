package com.example.fotpulse;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText nameInput, emailInput, indexNumberInput, passwordInput;
    private TextInputLayout nameInputLayout, emailInputLayout, indexNumberInputLayout, passwordInputLayout;
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

        nameInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        indexNumberInputLayout = findViewById(R.id.indexNumberInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        signUpButton = findViewById(R.id.signUpButton);

        // Set click listener for Sign Up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String indexNumber = indexNumberInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

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

                // Call Firebase sign-up method
                signUpUser(email, password, name, indexNumber);
            }
        });
    }

    private void signUpUser(String email, String password, String name, String indexNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        // Optionally, you can navigate to the next screen or finish this activity
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
