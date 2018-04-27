package com.example.kayangan.absencehrd.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.AlertDialogManager;
import com.example.kayangan.absencehrd.Helper.GPSTracker;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GPSTracker tracker;

    TextView NAMA;

    AlertDialogManager alert = new AlertDialogManager();
    SessionManager sessionManager;

    NavigationView navigationView;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        if (!sessionManager.isLoggedIn())
            redirectToLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        NAMA = headerView.findViewById(R.id.namaUSER);

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);

        NAMA.setText(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                MenuActivity.this.finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Intent intent;


        if (id == R.id.nav_task) {

        }
        else if (id == R.id.nav_absence) {
            if (tracker.inLocation)
            {
                intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "ADJUST YOUR POSITION FIRST!", Toast.LENGTH_SHORT).show();
                if (checkGPS())
                    startActivity(new Intent(this, MapsActivity.class));
            }
        }
        else if (id == R.id.nav_geotag) {
            intent = new Intent(MenuActivity.this, MapsActivity.class);
            if (checkGPS())
                startActivity(intent);

        }
        else if (id == R.id.nav_stock){
            startActivity(new Intent(MenuActivity.this, StockActivity.class));
        }
        else if (id == R.id.nav_sales_order){

        }
        else if (id == R.id.nav_profile) {
            intent = new Intent(MenuActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_out) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(false)
                    .setTitle("Log Out")
                    .setMessage("Are You sure want to log out?");

            alert.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sessionManager.logoutUser();
                            tracker.inLocation = false;
                            finish();
                        }
                    });

            alert.setNegativeButton("NO", null);

            AlertDialog dialog = alert.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void redirectToLogin() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private boolean checkGPS(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);


        if (!provider.equals("")){
            return true;
        }
        else
        {
            alert.setCancelable(false)
                    .setTitle("GPS Settings")
                    .setMessage("GPS is not enabled. Do you want to go to setting menu?");

            alert.setPositiveButton("SETTINGS",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            alert.setNegativeButton("CANCEL", null);

            AlertDialog dialog = alert.create();
            dialog.show();
        }

        return false;
    }
}
