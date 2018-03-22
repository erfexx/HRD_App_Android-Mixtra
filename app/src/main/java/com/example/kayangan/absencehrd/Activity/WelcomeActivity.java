package com.example.kayangan.absencehrd.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kayangan.absencehrd.R;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                SPLASH_TIME_OUT
        );

    }
}
