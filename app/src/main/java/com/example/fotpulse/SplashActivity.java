package com.example.fotpulse;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay the transition to MainActivity for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the delay
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);

                // Finish SplashActivity so the user cannot return to it
                finish();
            }
        }, 3000); // 3-second delay
    }
}