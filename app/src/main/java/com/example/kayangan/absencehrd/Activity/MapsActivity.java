package com.example.kayangan.absencehrd.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import com.example.kayangan.absencehrd.Helper.Constants;
import com.example.kayangan.absencehrd.Helper.GPSTracker;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.kayangan.absencehrd.R;

import java.util.Collections;

public class MapsActivity
        extends
        AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener,
        ResultCallback<Status>
{
    private GoogleApiClient apiClient;


    private GoogleMap googleMap;

    private boolean isMonitoring = false;
    private PendingIntent pendingIntent;

    Button btnSetLocation;

    double latitude = 0;
    double longitude = 0;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GPSTracker.inLocation= false;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, 100);

        btnSetLocation = findViewById(R.id.btnReqPos);

        btnSetLocation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLocationMonitor();

                        if (GPSTracker.inLocation)
                            Toast.makeText(MapsActivity.this, "In Location", Toast.LENGTH_SHORT).show();
                        else if (!GPSTracker.inLocation)
                            Toast.makeText(MapsActivity.this, "Not In Location", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("AAA", "Google Api Connected");
        isMonitoring = true;
        startGeofencing();
        startLocationMonitor();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("AAA", "Google Api Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isMonitoring = false;
        Log.d("AAA", "Connection Failed: " + connectionResult.getErrorMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.reconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        apiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);
        if (response != ConnectionResult.SUCCESS) {
            Log.d("AAA", "Google Play Service Not Available");
            GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, response, 1).show();
        } else {
            Log.d("AAA", "Google play service available");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap = googleMap;
        LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_COMP_LOC);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Mixtra Inti Tekindo"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18f));

        googleMap.setMyLocationEnabled(true);

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(Constants.GEOFENCE_RADIUS_IN_METERS)
                .strokeColor(Color.BLUE)
                .strokeWidth(7f))
                ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (isMonitoring) {
            menu.findItem(R.id.action_start_monitor).setVisible(false);
            menu.findItem(R.id.action_stop_monitor).setVisible(false);
        } else {
            menu.findItem(R.id.action_start_monitor).setVisible(false);
            menu.findItem(R.id.action_stop_monitor).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_monitor:
                startGeofencing();
                break;
            case R.id.action_stop_monitor:
                stopGeoFencing();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startLocationMonitor(){
        Log.d("AAA", "Start Location Monitor");
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d("AAA", "Location Change, Lat: "+location.getLatitude()+" Lng: "+ location.getLongitude());
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            if (location == null)
                            {
                                latitude = 37.4275;
                                longitude = -122.17;
                            }

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18f));
                        }
                    });
        }
        catch (SecurityException e){
            Log.d("AAA", e.getMessage());
        }
    }

    private Geofence getGeofence(){
        LatLng latLng = Constants.AREA_LANDMARKS.get(
                Constants.GEOFENCE_ID_COMP_LOC
        );

        return new Geofence.Builder()
                .setRequestId(Constants.GEOFENCE_ID_COMP_LOC)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setCircularRegion(latLng.latitude, latLng.longitude, Constants.GEOFENCE_RADIUS_IN_METERS)
                .setNotificationResponsiveness(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(Collections.singletonList(getGeofence()));
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        if (pendingIntent != null)
            return pendingIntent;
        Intent intent = new Intent(this, GPSTracker.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startGeofencing(){
        Log.d("AAA", "Start geofencing monitoring call");

        pendingIntent = getGeofencePendingIntent();

        if (!apiClient.isConnected()) {
            Log.d("AAA", "Google API client not connected");
        } else {
            try {
                LocationServices.GeofencingApi.addGeofences(
                        apiClient, getGeofencingRequest(),
                        pendingIntent).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.d("AAA", "Successfully Geofencing Connected");
                        } else {
                            Log.d("AAA", "Failed to add Geofencing " + status.getStatus());
                        }
                    }
                });
            } catch (SecurityException e) {
                Log.d("AAA", e.getMessage());
            }
        }
        isMonitoring = true;
        invalidateOptionsMenu();
    }

    private void stopGeoFencing() {
        pendingIntent = getGeofencePendingIntent();
        LocationServices.GeofencingApi.removeGeofences
                (apiClient, pendingIntent)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess())
                            Log.d("AAA", "Stop geofencing");
                        else
                            Log.d("AAA", "Not stop geofencing");
                    }
                });

        isMonitoring = false;
        invalidateOptionsMenu();
    }

}