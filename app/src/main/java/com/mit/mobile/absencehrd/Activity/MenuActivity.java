package com.mit.mobile.absencehrd.Activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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

import com.mit.mobile.absencehrd.Fragment.AttendanceFragment;
import com.mit.mobile.absencehrd.Fragment.DashboardFragment;
import com.mit.mobile.absencehrd.Fragment.MapsFragment;
import com.mit.mobile.absencehrd.Fragment.SalesOrderFragment;
import com.mit.mobile.absencehrd.Fragment.StockFragment;
import com.mit.mobile.absencehrd.Fragment.TaskManagerFragment;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.GPSTracker;
import com.mit.mobile.absencehrd.Helper.SynchronizeData;
import com.mit.mobile.absencehrd.R;
import com.mit.mobile.absencehrd.Helper.SessionManager;

import org.apache.commons.text.WordUtils;

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GPSTracker tracker;

    TextView NAMA;
    ImageView FOTO;

    SessionManager sessionManager;

    NavigationView navigationView;
    DrawerLayout drawer;
    View navHeader;
    Toolbar toolbar;
    public static int navItemIndex = 0;
    public static final String TAG_NOL = "nol", TAG_SATU = "satu",
            TAG_DUA = "dua", TAG_TIGA = "tiga", TAG_EMPAT = "empat",
            TAG_LIMA = "lima", TAG_ENAM = "enam", TAG_TUJUH = "tujuh";

    public static String CURRENT_TAG = TAG_SATU;
    private String[] activityTitles;
    boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    boolean doubleBackToExitPressedOnce = false;

    DatabaseHandler handler;

    Runnable runnable;
    Handler handlerr;

    CardView cvTM, cvA, cvS, cvSO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sessionManager = new SessionManager(this);
        handler = new DatabaseHandler(this);

        if (!sessionManager.isConnectedToNetwork())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

            builder.setCancelable(false)
                    .setTitle("Network Disconnect")
                    .setMessage("Not Connected to Network")
                    .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        SynchronizeData.getInstance(MenuActivity.this).LastAttRecord(Constants.currentUserID);

        if (!sessionManager.isLoggedIn())
            redirectToLogin();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
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
        final String name = user.get(SessionManager.KEY_NAME);
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

        activityTitles = getResources().getStringArray(R.array.activity_titles);
        mHandler = new Handler();


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.nav_dash:
                                navItemIndex = 0;
                                CURRENT_TAG = TAG_NOL;
                                break;
                            case R.id.nav_task:
                                navItemIndex = 1;
                                CURRENT_TAG = TAG_SATU;
                                break;
                            case R.id.nav_absence:
                            {
                                if (tracker.inLocation)
                                {
                                    navItemIndex = 2;
                                    CURRENT_TAG = TAG_DUA;
                                }
                                else 
                                {
                                    Toast.makeText(MenuActivity.this, "Can't Get The Last Position", Toast.LENGTH_SHORT).show();
                                }
                            }
                                break;
                            case R.id.nav_geotag:
                            {
                                if (checkGPS())
                                {
                                    navItemIndex = 3;
                                    CURRENT_TAG = TAG_TIGA;
                                }
                            }
                                break;
                            case R.id.nav_stock:
                                navItemIndex = 4;
                                CURRENT_TAG = TAG_EMPAT;
                                break;
                            case R.id.nav_sales_order:
                                navItemIndex = 5;
                                CURRENT_TAG = TAG_LIMA;
                                break;
                            case R.id.nav_profile:
                            {
                                navItemIndex = 6;
                                CURRENT_TAG = TAG_ENAM;
                            }
                                break;
                            case R.id.nav_out:
                            {
                                navItemIndex = 7;
                                CURRENT_TAG = TAG_TUJUH;

                                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
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

                                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        navItemIndex = 0;
                                        CURRENT_TAG = TAG_NOL;
                                        loadHomeFragment();
                                    }
                                });

                                AlertDialog dialog = alert.create();
                                dialog.show();
                            }
                                break;
                            default:
                                navItemIndex = 0;
                        }

                        /*if (item.isChecked())
                            item.setChecked(false);
                        else
                            item.setChecked(true);

                        item.setChecked(true);*/

                        loadHomeFragment();

                        return true;
                    }
                }
        );


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_NOL;
            loadHomeFragment();
        }

    }

    public void loadHomeFragment() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null)
        {
            drawer.closeDrawers();
            return;
        }

        Runnable mpend = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment, CURRENT_TAG);
                ft.commitAllowingStateLoss();
            }
        };

        if (mpend != null)
            mHandler.post(mpend);

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex){
            case 0:
                DashboardFragment zero = new DashboardFragment();
                return zero;

            case 1:
                TaskManagerFragment one = new TaskManagerFragment();
                return one;

            case 2:
            {
                AttendanceFragment two = new AttendanceFragment();
                return two;
            }

            case 3:
            {
                MapsFragment three = new MapsFragment();
                return three;
            }

            case 4:
                StockFragment four = new StockFragment();
                return four;

            case 5:
                SalesOrderFragment five = new SalesOrderFragment();
                return five;

            case 6:
            {
                Intent a = new Intent(MenuActivity.this, ProfileActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(MenuActivity.this, R.anim.fade_in, R.anim.fade_out);
                MenuActivity.this.startActivity(a, options.toBundle());
            }

                default:
                    return new DashboardFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (shouldLoadHomeFragOnBackPress){
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_NOL;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
        /*else {

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
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (navItemIndex == 0)
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

        if (id == R.id.nav_task) {}
        else if (id == R.id.nav_absence) {}
        else if (id == R.id.nav_geotag) {}
        else if (id == R.id.nav_stock){}
        else if (id == R.id.nav_sales_order){}
        else if (id == R.id.nav_profile) {}
        else if (id == R.id.nav_out) {}

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
