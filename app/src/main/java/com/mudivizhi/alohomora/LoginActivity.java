package com.mudivizhi.alohomora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoginActivity extends AppCompatActivity {

    LinearLayout signupLayout;
    ImageView mobileSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupLayout = findViewById(R.id.signup_layout);
        mobileSubmit = findViewById(R.id.iv_mobileSubmit);

        signupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity();
            }
        });

        mobileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity();
            }
        });


    }

    private void moveToNextActivity() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
