package com.mit.mobile.absencehrd.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mit.mobile.absencehrd.Fragment.AttendanceFragment;
import com.mit.mobile.absencehrd.Model.LocalData;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotification(context, AttendanceFragment.class,
                "Check Out now?", "Have you checked out today?");
    }
}
