package com.example.kayangan.absencehrd.Helper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.Model.Coordinates;
import com.example.kayangan.absencehrd.Model.DummyModel;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SynchronizeData {
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    @SuppressLint("StaticFieldLeak")
    private static SynchronizeData mInstance;

    SQLiteOpenHelper helper;
    private SQLiteDatabase database;

    DatabaseHandler handler;

    private String linkLocation  = Constants.url+"locations";
    private String linkAttendance = Constants.url+"attendances";
    private String linkStock = Constants.url+"stocks";

    private Coordinates coordinates;
    private AttendanceRecord attRecord;
    private DummyModel stockRecord;

    private SynchronizeData(Context context)
    {
        mCtx = context;
    }

    public static synchronized SynchronizeData getInstance(Context context){
        if (mInstance == null)
            mInstance = new SynchronizeData(context);

        return mInstance;
    }

    public void SyncAll(){
        SyncAttendance();
        SyncLocation();
        SyncStock();

        Toast.makeText(mCtx, "SYNC COMPLETE", Toast.LENGTH_SHORT).show();
    }

    private void SyncStock(){
        helper = new DatabaseHandler(mCtx);
        database = helper.getWritableDatabase();

        JsonArrayRequest arrayReq = new JsonArrayRequest(
                linkStock,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i<response.length(); i++)
                            {
                                stockRecord = new DummyModel();

                                JSONObject obj = response.getJSONObject(i);

                                String a = obj.getString("id");
                                String b = obj.getString("item");
                                String c = obj.getString("category");
                                String d = obj.getString("branch");
                                String e = obj.getString("department");
                                String f = obj.getString("price");

                                stockRecord.setId(Integer.parseInt(a));
                                stockRecord.setItem(b);
                                stockRecord.setCategory(c);
                                stockRecord.setBranch(d);
                                stockRecord.setDepartment(e);
                                stockRecord.setPrice(Integer.parseInt(f));

                                insertToDatabaseStocks(stockRecord);

                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e("SYNC DATA", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SYNC DATA", error.toString());
                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(arrayReq);
    }

    public void SyncLocation(){

        helper = new DatabaseHandler(mCtx);
        database = helper.getWritableDatabase();

        JsonArrayRequest arrayReq = new JsonArrayRequest(
                linkLocation,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i<response.length(); i++)
                            {
                                coordinates = new Coordinates();
                                JSONObject obj = response.getJSONObject(i);

                                String placeName = obj.getString("place");
                                String placeId = obj.getString("id");
                                String placeZone = obj.getString("zone");
                                String placeLat = obj.getString("latitude");
                                String placeLong = obj.getString("longitude");

                                coordinates.setName(placeName);
                                coordinates.setZone(placeZone);
                                coordinates.setId(Integer.parseInt(placeId));
                                coordinates.setLatLng(
                                        new LatLng(
                                                Double.parseDouble(placeLat),
                                                Double.parseDouble(placeLong)
                                        )
                                );

                                insertToDatabaseLocations(coordinates);
                                Toast.makeText(mCtx, "LOCATION UPDATED!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e("SYNC DATA", e.toString());
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SYNC DATA", error.toString());
                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(arrayReq);
    }

    //GET ALL ATTENDANCE RECORD FROM DB SERVER
    private void SyncAttendance(){
        helper = new DatabaseHandler(mCtx);
        database = helper.getWritableDatabase();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                linkAttendance,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i<response.length(); i++)
                            {
                                attRecord = new AttendanceRecord();

                                JSONObject obj = response.getJSONObject(i);

                                String id = obj.getString("id");
                                String user_id = obj.getString("user_id");
                                String date = formatDate(obj.getString("date").substring(0, 10));
                                String clock_in = obj.getString("clock_in");
                                String clock_out = obj.getString("clock_out");

                                attRecord.setId(Integer.parseInt(id));
                                attRecord.setUser_id(user_id);
                                attRecord.setDate(date);
                                attRecord.setClock_in(clock_in);
                                attRecord.setClock_out(clock_out);

                                insertToDatabaseAttendances(attRecord);
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e("SYNC DATA", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SYNC DATA", error.toString());
                    }
                }
        );

        arrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        AppController.getInstance(mCtx).addToRequestque(arrayRequest);

    }

    //POST DATA TO DB SERVER
    public void ExportAttendanceOut(final AttendanceRecord rec){
        String link = Constants.url+"attendances";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("UPLOAD", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UPLOAD", error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("id","");
                parameter.put("date", formatDate1(rec.getDate()));
                parameter.put("clock_in", rec.getClock_in());
                parameter.put("clock_out", rec.getClock_out());
                parameter.put("user_id", rec.getUser_id());

                return parameter;
            }
        };
        Log.i("TANGGAL", formatDate1(rec.getDate()));
        AppController.getInstance(mCtx).addToRequestque(request);
    }

    //GET ALL ATTENDANCE RECORD FROM SQLITE
    public void AttOut() {
        handler = new DatabaseHandler(mCtx);

        Cursor record = handler.getAllAttendance();

        if (record.getCount() > 0)
        {
            while (record.moveToNext())
            {
                attRecord = new AttendanceRecord(
                        record.getString(1),
                        record.getString(2),
                        record.getString(3),
                        record.getString(4)
                );

                ExportAttendanceOut(attRecord);
            }
        }
    }

    //UPDATE ATTENDANCE RECORD ON DB SERVER
    public void AttUpd(final String clock_out, final String date){
        String link = Constants.url+"attendances/"+Constants.currentUserID;

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("UPDATE", "COMPLETE "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("UPDATE", "ERROR "+error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clock_out", clock_out);
                params.put("date", date);

                return params;
            }
        };

        AppController.getInstance(mCtx).addToRequestque(request);
    }



    //INSERT TO SQLITE FROM DB SERVER
    private void insertToDatabaseAttendances(AttendanceRecord attRecord) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHandler.ATT_DATE, attRecord.getDate());
        cv.put(DatabaseHandler.ATT_ID, attRecord.getId());
        cv.put(DatabaseHandler.ATT_IN, attRecord.getClock_in());
        cv.put(DatabaseHandler.ATT_OUT, attRecord.getClock_out());
        cv.put(DatabaseHandler.ATT_USER_ID, attRecord.getUser_id());

        database.insert(DatabaseHandler.TABLE_ATTENDANCES, null, cv);
        Log.i("AAA", "INSERTED TO DATABASE");
    }

    private void insertToDatabaseLocations(Coordinates c) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHandler.LOC_ID, c.getId());
        cv.put(DatabaseHandler.LOC_NAME, c.getName());
        cv.put(DatabaseHandler.LOC_ZONE_TYPE, c.getZone());
        cv.put(DatabaseHandler.LOC_LAT, c.getLatLng().latitude);
        cv.put(DatabaseHandler.LOC_LONG, c.getLatLng().longitude);

        database.insert(DatabaseHandler.TABLE_LOCATIONS, null, cv);
    }

    private void insertToDatabaseStocks(DummyModel st) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHandler.STOCK_ID, st.getId());
        cv.put(DatabaseHandler.STOCK_ITEM, st.getItem());
        cv.put(DatabaseHandler.STOCK_BRANCH, st.getBranch());
        cv.put(DatabaseHandler.STOCK_CATEGORY, st.getCategory());
        cv.put(DatabaseHandler.STOCK_DEPARTMENT, st.getDepartment());
        cv.put(DatabaseHandler.STOCK_PRICE, st.getPrice());

        database.insert(DatabaseHandler.TABLE_STOCKS, null, cv);
    }



    private String formatDate(String date){
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");

        try {
            String a = df2.format(df1.parse(date));
            return a;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String formatDate1(String date){
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        try {
            String a = df2.format(df1.parse(date));
            return a;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
