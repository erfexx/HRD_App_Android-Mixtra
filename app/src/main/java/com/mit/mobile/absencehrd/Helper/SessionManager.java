package com.mit.mobile.absencehrd.Helper;

/**
 * Created by Kevin E on 3/15/2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mit.mobile.absencehrd.Activity.HomeActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AbsentApp";

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_TAP_IN = "IsTappedIn";
    private static final String IS_TAP_OUT = "IsTappedOut";

    //Buat welcome screen
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "id";
    public static final String KEY_ZONE = "zone";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";



    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String NAMA, String id, String zone, String gender){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, NAMA);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ZONE, zone);
        editor.putString(KEY_GENDER, gender);

        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, HomeActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_ZONE, pref.getString(KEY_ZONE, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_LAT, pref.getString(KEY_LAT, null));
        user.put(KEY_LONG, pref.getString(KEY_LONG, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, HomeActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //clear current user id
        Constants.currentUserID = "";

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createTapInSession(){
        editor.putBoolean(IS_TAP_IN, true);

        editor.commit();
    }

    public void createPosSession(String lat, String longi){
        editor.putString(KEY_LAT, lat);
        editor.putString(KEY_LONG, longi);

        editor.commit();
    }

    public void createTapOutSession(){
        editor.putBoolean(IS_TAP_IN, false);
        editor.commit();
    }

    public boolean isTappedIn(){
        return pref.getBoolean(IS_TAP_IN, false);
    }

    public boolean isConnectedToNetwork(){
        ConnectivityManager cM = (ConnectivityManager) _context.getSystemService(_context.CONNECTIVITY_SERVICE);

        if (cM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                cM.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED )
        {
            return true;
        }
        return false;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }



}