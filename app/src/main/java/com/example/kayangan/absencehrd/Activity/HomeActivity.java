package com.example.kayangan.absencehrd.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.SessionManager;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.currentUser;

public class HomeActivity extends AppCompatActivity {
    SessionManager sessionManager;

    RelativeLayout relativeLayout;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button toLogin, toRegist;

        toLogin = findViewById(R.id.btnToLogin);
        toRegist = findViewById(R.id.btnToRegis);

        Log.e("AAA", ""+ currentUser.currentUserID);

        relativeLayout = findViewById(R.id.relativeLayout);

        if (!sessionManager.isConnectedToNetwork())
        {
            Snackbar snackbar = Snackbar.make(relativeLayout, "Network Is Not Available!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sessionManager.isConnectedToNetwork();
                            startActivity(new Intent(getBaseContext(), HomeActivity.class));
                        }
                    });
            snackbar.show();
        }

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}