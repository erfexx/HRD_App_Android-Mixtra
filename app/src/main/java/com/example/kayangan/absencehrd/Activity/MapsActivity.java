package com.example.kayangan.absencehrd.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.GPSTracker;
import com.example.kayangan.absencehrd.Helper.SessionManager;
import com.example.kayangan.absencehrd.Helper.currentUser;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.kayangan.absencehrd.R;

import java.util.HashMap;
import java.util.Random;

public class MapsActivity
        extends
        AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{
  private Button btnReqPos;

  GPSTracker gpsTracker;
  private GoogleMap mMap;

  //Play Services Location
  private static final int MY_PERMISSION_REQUEST_CODE = 1706;
  private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 170697;

  private LocationRequest locationRequest;
  private GoogleApiClient apiClient;
  private Location lastLocation;


  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

  private static int UPDATE_INTERVAL = 5000;
  private static int FATEST_INTERVAL = 3000;
  private static int DISPLACEMENT = 10;


  DatabaseReference databaseReference;
  GeoFire geoFire;

  Marker mCurrentPos;

  SessionManager sessionManager;


  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);


    databaseReference = FirebaseDatabase.getInstance().getReference("" + currentUser.currentUserID);
    geoFire = new GeoFire(databaseReference);

    setUpLocation();

    btnReqPos = findViewById(R.id.btnReqPos);
    btnReqPos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setUpLocation();
      }
    });
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    LatLng dangerArea = new LatLng(-6.190361, 106.748589);

    mMap.addCircle(
            new CircleOptions()
                    .center(dangerArea)
                    .radius(100)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(7.0f)
    );


    //GeoQuery
    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(dangerArea.latitude, dangerArea.longitude), 0.1f);
    geoQuery.addGeoQueryEventListener(
            new GeoQueryEventListener() {
              @Override
              public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Employee Attendance", String.format("%s are in office area", key));
                gpsTracker.inLocation = true;
              }

              @Override
              public void onKeyExited(String key) {
                sendNotification("Employee Attendance", String.format("%s is no longer in the office area", key));
                gpsTracker.inLocation = false;
              }

              @Override
              public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE", String.format("%s moved within the office area [%f/%f]", key, location.latitude, location.longitude));
              }

              @Override
              public void onGeoQueryReady() {

              }

              @Override
              public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR", ""+error);
              }
            }
    );

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode)
    {
      case MY_PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
          if (checkPlayServices()){
            buildGoogleApiClient();
            createLocationRequest();
            displayLocation();
          }
        }
        break;
    }
  }






  ////////////////////////////////////////////////////////////////////////////////////////////////
  @Override
  public void onLocationChanged(Location location) {
    lastLocation = location;
    displayLocation();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    displayLocation();
    startLocationUpdates();
  }

  @Override
  public void onConnectionSuspended(int i) {
    apiClient.connect();
  }






  ////////////////////////////////////////////////////////////////////////////////////////////////
  @SuppressLint("RestrictedApi")
  private void createLocationRequest() {
    locationRequest = new LocationRequest();

    locationRequest.setInterval(UPDATE_INTERVAL);
    locationRequest.setFastestInterval(FATEST_INTERVAL);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    locationRequest.setSmallestDisplacement(DISPLACEMENT);
  }

  private void buildGoogleApiClient() {
    apiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();

    apiClient.connect();
  }

  private boolean checkPlayServices() {
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

    if (resultCode != ConnectionResult.SUCCESS)
    {
      if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
        GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
      else
      {
        Toast.makeText(this, "This Device is not supported", Toast.LENGTH_SHORT).show();
        finish();
      }

      return false;
    }
    return true;
  }

  private void setUpLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
      //Request Runtime Permission
      ActivityCompat.requestPermissions(
              this,
              new String[]{
                      Manifest.permission.ACCESS_COARSE_LOCATION,
                      Manifest.permission.ACCESS_FINE_LOCATION
              },
              MY_PERMISSION_REQUEST_CODE
      );
    }
    else
    {
      if (checkPlayServices()){
        buildGoogleApiClient();
        createLocationRequest();
        displayLocation();
      }
    }
  }

  private void displayLocation() {
    sessionManager = new SessionManager(getApplicationContext());

      HashMap<String, String> user = sessionManager.getUserDetails();
      String KEY = user.get(SessionManager.KEY_NAME);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
      return;
    }

    lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

    if (lastLocation != null)
    {
      final double latitude = lastLocation.getLatitude();
      final double longitude = lastLocation.getLongitude();

      //update to Firebase
      geoFire.setLocation(""+KEY, new GeoLocation(latitude, longitude),
              new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                  //Add Marker
                  if (mCurrentPos != null)
                  {
                    //remove old marker
                    mCurrentPos.remove();
                  }

                  //make new marker
                  mCurrentPos = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You"));

                  //move camera to current position
                  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
                }
              });

      Log.d("AAA", "Lokasi diubah: " + latitude + "  " + longitude);
    }
    else
    {
      Log.d("AAA", "Lokasi tidak ditemukan ha 1");
    }
  }

  private void startLocationUpdates() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
      return;
    }

    LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
  }

  private void sendNotification(String title, String content) {
    Notification.Builder builder = new Notification.Builder(this)
            .setSmallIcon(R.drawable.logo_round)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(false);

    NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

    Intent intent = new Intent(this, MapsActivity.class);
    PendingIntent contentIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_IMMUTABLE);

    builder.setContentIntent(contentIntent);

    Notification notification = builder.build();

    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    notification.defaults |= Notification.DEFAULT_SOUND;

    manager.notify(new Random().nextInt(), notification);
  }





  ////////////////////////////////////////////////////////////////////////////////////////////////
  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }
}