package com.example.kayangan.absencehrd.Helper;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kayangan.absencehrd.Activity.MapsActivity;
import com.example.kayangan.absencehrd.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;
import java.util.Random;

public class GPSTracker extends IntentService{
  public static boolean inLocation = false;

  private static final String TAG = "AAA";

  public GPSTracker() {
    super(TAG);
  }

  protected void onHandleIntent(@Nullable Intent intent) {
    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
    if (geofencingEvent.hasError()) {
      Log.d("AAA", "GeofencingEvent error " + geofencingEvent.getErrorCode());
    }else{
      int transaction = geofencingEvent.getGeofenceTransition();
      List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
      Geofence geofence = geofences.get(0);
      if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER && geofence.getRequestId().equals(Constants.GEOFENCE_ID_COMP_LOC)) {
        Log.d("AAA", "You are inside Mixtra Inti Tekindo");
        sendNotification("Location Acquired", "You are inside Mixtra Inti Tekindo");
        inLocation = true;
      } else {
        Log.d("AAA", "You are outside Mixtra Inti Tekindo");
        sendNotification("Location Acquired", "You are outside Mixtra Inti Tekindo");
        inLocation = false;
      }
    }
  }


  private void sendNotification(String title, String content) {
    Notification.Builder builder = new Notification.Builder(this)
            .setSmallIcon(R.drawable.ic_near_me_orange_24dp)
            .setColor(Color.rgb(255, 110, 64))
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
}
