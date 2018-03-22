package com.example.kayangan.absencehrd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kayangan.absencehrd.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        Button toLogin, toRegist;

        toLogin = findViewById(R.id.btnToLogin);
        toRegist = findViewById(R.id.btnToRegis);

        toLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );

        toRegist.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}