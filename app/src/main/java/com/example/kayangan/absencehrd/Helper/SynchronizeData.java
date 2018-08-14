package com.example.kayangan.absencehrd.Helper;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.Model.Comment;
import com.example.kayangan.absencehrd.Model.Coordinates;
import com.example.kayangan.absencehrd.Model.DummyModel;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.Model.User;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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

    private DatabaseHandler handler;

    private String linkLocation  = Constants.url+"locations";
    private String linkAttendance = Constants.url+"attendances";
    private String linkStock = Constants.url+"stocks";
    private String linkTask = Constants.url+"taskmanagers";
    private String linkUser = Constants.url+"employees";
    private String linkComm = Constants.url+"comments";

    private Coordinates coordinates;
    private AttendanceRecord attRecord;
    private DummyModel stockRecord;
    private Task taskRecord;
    private User userRecord;
    private Comment comRecord;

    private SessionManager session;

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
        handler = new DatabaseHandler(mCtx);

        //SyncAttendance();
        SyncLocation();
        //SyncStock();

        if (handler.isTableExists())
            syncUsers();

        //syncTasks();

        Toast.makeText(mCtx, "SYNC COMPLETE", Toast.LENGTH_SHORT).show();
    }

    //upload comment
    public void syncCommentOut(final Comment comment) {
        String link = Constants.url+"comments";

        final String a = comment.getTitle();
        final String b = String.valueOf(comment.getCommenter());
        final String c = String.valueOf(comment.getComment());
        final String d = String.valueOf(comment.getTask_id());
        final String e = String.valueOf(comment.getCreated_at());

        StringRequest req = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("COMMENTSs", response);
                        Toast.makeText(mCtx, ""+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("COMMENTSsss", error.toString());
                        error.printStackTrace();
                        Toast.makeText(mCtx, "ERROR "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("comment1", ""+c);
                parameter.put("title", ""+a);
                parameter.put("task_id", ""+d);
                parameter.put("commenter", ""+b);
                parameter.put("created_at", ""+e);
                parameter.put("updated_at", ""+e);

                return parameter;
            }
        };

        AppController.getInstance(mCtx).addToRequestque(req);
    }

    //download comment
    public void SyncComment(int id){
        String linkComments = Constants.url+"comments/"+id;
        helper = new DatabaseHandler(mCtx);
        database = helper.getWritableDatabase();

        JsonArrayRequest req = new JsonArrayRequest(
                linkComments,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i<response.length(); i++)
                            {
                                comRecord = new Comment();

                                JSONObject obj = response.getJSONObject(i);

                                String a = obj.getString("id");
                                String b = obj.getString("comment1");
                                String c = obj.getString("title");
                                String d = obj.getString("task_id");
                                String e = obj.getString("commenter");
                                String f = obj.getString("created_at");
                                String g = obj.getString("updated_at");

                                comRecord.setId(Integer.parseInt(a));
                                comRecord.setComment(b);
                                comRecord.setTitle(c);
                                comRecord.setTask_id(Integer.parseInt(d));
                                comRecord.setCommenter(e);
                                comRecord.setCreated_at(f);
                                comRecord.setUpdated_at(g);

                                insertToDatabaseComments(comRecord);
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

                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(req);

    }

    //download stock
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

                                //insertToDatabaseStocks(stockRecord);

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

    //download location
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

                                //insertToDatabaseLocations(coordinates);
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

    //download user
    public void syncUsers(){
        helper = new DatabaseHandler(mCtx);
        database = helper.getWritableDatabase();

        Log.i("USERSYNC", "TEST JADI");

        JsonArrayRequest request = new JsonArrayRequest(
                linkUser,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length(); i++)
                            {
                                userRecord = new User();

                                JSONObject object = response.getJSONObject(i);

                                String name = object.getString("name");
                                String pass = object.getString("password");
                                String id = object.getString("employeeID");
                                String mod = object.getString("modifiedDate");
                                String tim = object.getString("timestamp");

                                userRecord.setName(name);
                                userRecord.setPassword(pass);
                                userRecord.setId(Integer.parseInt(id));
                                userRecord.setModified_date(mod);
                                userRecord.setTimestamp(tim);

                                insertToDatabaseUsers(userRecord);

                                Log.i("USERSYNC", "USER DATA UPDATED");
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("USERSYNC", error.toString());
                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(request);
    }


    //download attendance
    public void SyncAttendance(String id){
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

                                String id = obj.getString("attendanceID");
                                String employeeID = obj.getString("employeeID");
                                String date = formatDate(obj.getString("modifiedDate").substring(0, 10));
                                String checkTime = obj.getString("checkTime");
                                String attendanceType = obj.getString("attendanceType");
                                String timestamp = obj.getString("timestamp");

                                attRecord.setId(id);
                                attRecord.setEmployeeID(employeeID);
                                attRecord.setModifiedDate(date);
                                attRecord.setCheckTime(checkTime);
                                attRecord.setAttendanceType(attendanceType);
                                attRecord.setTimestamp(timestamp);

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



    //upload attendance
    public void PostAttendance(final AttendanceRecord rec){
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

                parameter.put("attendanceType", rec.getAttendanceType());
                parameter.put("checkTime", rec.getCheckTime());
                parameter.put("employeeID", rec.getEmployeeID());
                parameter.put("modifiedDate", formatDate1(rec.getModifiedDate()));
                parameter.put("attendanceID", rec.getId());

                return parameter;
            }
        };
        Log.i("TANGGAL", formatDate1(rec.getModifiedDate()));
        AppController.getInstance(mCtx).addToRequestque(request);
    }

    //GET ALL ATTENDANCE RECORD FROM SQLITE
    /*public void AttOut() {
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
    }*/

    //upload update attendance
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


    /*ddededede

    //download task
    public void syncTasks(){
        handler = new DatabaseHandler(mCtx);
        database = handler.getWritableDatabase();
        Log.i("SINKRONTASK", "TEST MASUK");
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                linkTask,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i=0; i<response.length(); i++)
                            {
                                taskRecord = new Task();

                                JSONObject object = response.getJSONObject(i);

                                String taskId = object.getString("id");
                                String taskName = object.getString("tname");
                                String taskDesc = object.getString("tdesc");
                                String taskDueDate = object.getString("tduedate").substring(0,10);
                                String taskAssign = object.getString("tassign");
                                String taskProgress = object.getString("tprogress");
                                String taskAssign_by = object.getString("tassign_by");

                                taskRecord.set_id(Integer.parseInt(taskId));
                                taskRecord.setTname(taskName);
                                taskRecord.setTdesc(taskDesc);
                                taskRecord.setTduedate(taskDueDate);
                                taskRecord.setTassign(Integer.parseInt(taskAssign));
                                taskRecord.setTprogress(Integer.parseInt(taskProgress));
                                taskRecord.setTassign_by(Integer.parseInt(taskAssign_by));

                                insertToDatabaseTasks(taskRecord);

                                Log.i("TASKMANAGERSsync", taskRecord.toString());
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", error.toString());
                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(arrayRequest);
    }
    //upload task
    public void uploadTask(final Task task) {
        String link = Constants.url+"taskmanagers";

        StringRequest req = new StringRequest(
                Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TASKMANAGERS", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TASKMANAGERS", error.toString());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("id","");
                parameter.put("tname", task.getTname());
                parameter.put("tdesc", task.getTdesc());
                parameter.put("tduedate", task.getTduedate());
                parameter.put("tassign", String.valueOf(task.getTassign()));
                parameter.put("tassign_by", String.valueOf(task.getTassign_by()));
                parameter.put("tprogress", String.valueOf(task.getTprogress()));
                parameter.put("created_at", "");
                parameter.put("updated_at", "");

                return parameter;
            }
        };

        AppController.getInstance(mCtx).addToRequestque(req);
    }
    //delete task
    public void TaskDel(int pos) {
        String link = linkTask+"/"+pos;

        StringRequest req = new StringRequest(
                Request.Method.DELETE,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("DELETETASK", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DELETETASK", error.toString());
                        error.printStackTrace();
                    }
                }
        );

        Log.i("TASKPARAMS", link);
        AppController.getInstance(mCtx).addToRequestque(req);
    }
    //edit task
    public void TaskEdit(final Task task, int id) {
        String link = linkTask+"/"+id;

        StringRequest req = new StringRequest(
                Request.Method.PUT,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("EDITTASK", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("EDITTASK", error.toString());
                        error.printStackTrace();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("tname", task.getTname());
                params.put("tprogress", String.valueOf(task.getTprogress()));

                return params;
            }
        };

        AppController.getInstance(mCtx).addToRequestque(req);
    }

    qd3wdde*/




    //INSERT TO SQLITE FROM DB SERVER
    private void insertToDatabaseAttendances(AttendanceRecord attRecord) {
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHandler.ATT_MOD_DATE, attRecord.getModifiedDate());
        cv.put(DatabaseHandler.ATT_EMP_ID, attRecord.getEmployeeID());
        cv.put(DatabaseHandler.ATT_ID, attRecord.getId());
        cv.put(DatabaseHandler.ATT_CHECK_TIME, attRecord.getCheckTime());
        cv.put(DatabaseHandler.ATT_TYPE, attRecord.getAttendanceType());
        cv.put(DatabaseHandler.ATT_TIMESTAMP, attRecord.getTimestamp());

        database.insert(DatabaseHandler.TABLE_ATTENDANCES, null, cv);
        Log.i("AAA", "INSERTED TO DATABASE");
    }

    /*wdewdwdwdwdw

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

    private void insertToDatabaseTasks(Task taskRecord) {
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHandler.TASK_ID, taskRecord.get_id());
        cv.put(DatabaseHandler.TASK_TNAME, taskRecord.getTname());
        cv.put(DatabaseHandler.TASK_TDESC, taskRecord.getTdesc());
        cv.put(DatabaseHandler.TASK_TASSIGN, taskRecord.getTassign());
        cv.put(DatabaseHandler.TASK_TDUEDATE, taskRecord.getTduedate());
        cv.put(DatabaseHandler.TASK_TPROGRESS, taskRecord.getTprogress());
        cv.put(DatabaseHandler.TASK_TASSIGN_BY, taskRecord.getTassign_by());

        long id = database.insert(DatabaseHandler.TABLE_TASKS, null, cv);

        Log.i("TASKMANAGERSSSS", taskRecord.getTduedate() + " " + id);
    }

    cdjeiadjeijiejie*/


    private void insertToDatabaseUsers(User userRecord) {
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHandler.EMP_NAME, userRecord.getName());
        cv.put(DatabaseHandler.EMP_PASS, userRecord.getPassword());
        cv.put(DatabaseHandler.EMP_ID, userRecord.getId());
        cv.put(DatabaseHandler.EMP_TIMESTAMP, userRecord.getTimestamp());
        cv.put(DatabaseHandler.EMP_MODIFIED_DATE, userRecord.getModified_date());

        database.insert(DatabaseHandler.TABLE_EMPLOYEES, null, cv);
        Log.i("USERSYNC", userRecord.toString());
    }

    public void insertToDatabaseComments(Comment comRecord) {
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHandler.COM_ID, comRecord.getId());
        cv.put(DatabaseHandler.COM_COMMENT, comRecord.getComment());
        cv.put(DatabaseHandler.COM_COMMENTER, comRecord.getCommenter());
        cv.put(DatabaseHandler.COM_TASK_ID, comRecord.getTask_id());
        cv.put(DatabaseHandler.COM_TITLE, comRecord.getTitle());
        cv.put(DatabaseHandler.COM_CREATED_AT, comRecord.getCreated_at());
        cv.put(DatabaseHandler.COM_UPDATED_AT, comRecord.getUpdated_at());

        long id = database.insert(DatabaseHandler.TABLE_COMMENTS, null, cv);

        Log.i("COMMENTS", ""+id);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
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


    public void LastAttRecord(String currentUserID) {

        session = new SessionManager(mCtx);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                linkAttendance + "/" + currentUserID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("RESPONSES", response.getString("attendanceType"));

                            String a = response.getString("attendanceType");

                            if(a.equals("IN"))
                                session.createTapInSession();
                            else if(a.equals("OUT"))
                                session.createTapOutSession();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        AppController.getInstance(mCtx).addToRequestque(request);
    }
}
