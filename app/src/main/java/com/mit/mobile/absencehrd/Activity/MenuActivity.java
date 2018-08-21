package com.mit.mobile.absencehrd.Activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Helper.AlertDialogManager;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.GPSTracker;
import com.mit.mobile.absencehrd.Helper.SynchronizeData;
import com.mit.mobile.absencehrd.Model.SalesOrder;
import com.mit.mobile.absencehrd.R;
import com.mit.mobile.absencehrd.Helper.SessionManager;

import org.apache.commons.text.WordUtils;

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GPSTracker tracker;

    TextView NAMA;
    ImageView FOTO;

    AlertDialogManager alert = new AlertDialogManager();
    SessionManager sessionManager;

    NavigationView navigationView;
    CardView cvTM, cvA, cvS, cvSO, cvP;

    boolean doubleBackToExitPressedOnce = false;

    DatabaseHandler handler;

    ProgressDialog dialog;

    Runnable runnable;
    Handler handlerr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sessionManager = new SessionManager(this);
        handler = new DatabaseHandler(this);

        if (handler.isTableTaskExists())
            //SynchronizeData.getInstance(MenuActivity.this).syncTasks();


        cvTM = findViewById(R.id.taskID);
        cvA = findViewById(R.id.attendanceID);
        cvS = findViewById(R.id.stockID);
        cvSO = findViewById(R.id.salesorderID);
        //cvP = findViewById(R.id.profileID);

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

        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);

        View headerView = navigationView.getHeaderView(0);
        NAMA = findViewById(R.id.namaUSER);
        FOTO = findViewById(R.id.fotoUser);
        Resources resources = getResources();
        NAMA.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent p = new Intent(MenuActivity.this, ProfileActivity.class);
                        ActivityOptions a = ActivityOptions.makeCustomAnimation(MenuActivity.this, R.anim.fade_in, R.anim.fade_out);
                        MenuActivity.this.startActivity(p, a.toBundle());
                    }
                }
        );

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);
        String aa = WordUtils.capitalizeFully(name);
        String sex = user.get(SessionManager.KEY_GENDER);
        Constants.currentUserID = user.get(SessionManager.KEY_ID);

        runnable = new Runnable() {
            @Override
            public void run() {
                SynchronizeData.getInstance(MenuActivity.this).LastAttRecord(Constants.currentUserID);
                handlerr.postDelayed(runnable, 3000);
            }
        };

        handlerr = new Handler();
        handlerr.postDelayed(runnable, 3000);

        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.userimage);


        if (sex.equals("FEMALE")) {
            FOTO.setImageDrawable(resources.getDrawable(R.drawable.userimagewoman));
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.userimagewoman);
        }
        else if (sex.equals("MALE")) {
            FOTO.setImageDrawable(resources.getDrawable(R.drawable.userimage));
        }

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        drawable.setCircular(true);

        FOTO.setImageDrawable(drawable);
        NAMA.setText(aa);
        cvTM.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(MenuActivity.this, TaskManagerActivity.class));
                    }
                }
        );
        cvSO.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(MenuActivity.this, SalesOrderActivity.class));
                    }
                }
        );
        cvA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tracker.inLocation)
                {
                    Intent p = new Intent(MenuActivity.this, MainActivity.class);
                    ActivityOptions a = ActivityOptions.makeCustomAnimation(MenuActivity.this, R.anim.fade_in, R.anim.fade_out);
                    MenuActivity.this.startActivity(p, a.toBundle());
                }
                else{
                    Toast.makeText(getApplicationContext(), "ADJUST YOUR POSITION FIRST!", Toast.LENGTH_SHORT).show();
                    /*if (checkGPS())
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));*/
                }
            }
        });
        cvS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MenuActivity.this, StockActivity.class));
            }
        });
        /*cvP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
            }
        });*/
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
        if (id == R.id.action_sync) {

            //SynchronizeData.getInstance(MenuActivity.this).SyncAll();
            SynchronizeData.getInstance(MenuActivity.this).SyncAttendance(Constants.currentUserID);
            return true;
        }
        else if (id == R.id.to_profile){
            startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
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
            intent = new Intent(MenuActivity.this, TaskManagerActivity.class);
            //startActivity(intent);

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
            if (checkGPS()) {
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_stock){
            //startActivity(new Intent(MenuActivity.this, StockActivity.class));
        }
        else if (id == R.id.nav_sales_order){
            //startActivity(new Intent(MenuActivity.this, SalesOrderActivity.class));

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