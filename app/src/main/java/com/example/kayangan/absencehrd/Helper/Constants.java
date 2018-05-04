package com.example.kayangan.absencehrd.Helper;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {
    public static final String GEOFENCE_ID_COMP_LOC = "STAN_UNI";
    public static final float GEOFENCE_RADIUS_IN_METERS = 50;

    /**
     * Map for storing information about stanford university in the Stanford.
     */
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.
        AREA_LANDMARKS.put(GEOFENCE_ID_COMP_LOC, new LatLng(-6.190362, 106.748611));
    }
}
