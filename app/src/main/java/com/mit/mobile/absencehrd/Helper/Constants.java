package com.mit.mobile.absencehrd.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class Constants {
    public static final String GEOFENCE_ID_COMP_LOC = "MIXTRA COMPANY ";
    public static final float GEOFENCE_RADIUS_IN_METERS = 50;
    public static String currentUserID = "";
    public static String currentTIME = "";

    //setting ip address web service
    public static String url = "http://172.16.1.62:45455/api/";

    private static Context mCtx;
    private static Constants mInstance;

    private Constants(Context context){mCtx = context;}
    public static synchronized Constants getInstance(Context context){
        if (mInstance == null)
            mInstance = new Constants(context);

        return mInstance;
    }
    public void getServerTime(){
        String linktime = Constants.url+"time";

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                linktime,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jam = response.getString("jam");
                            Constants.currentTIME = jam;
                            Log.i("JAM", jam);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JAM", error.toString());
                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(objectRequest);
    }
    public static String encryptPassword(String s, byte[] salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(s.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    public static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }

}
