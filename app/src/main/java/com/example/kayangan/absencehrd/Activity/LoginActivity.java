package com.example.kayangan.absencehrd.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kayangan.absencehrd.Helper.AlertDialogManager;
import com.example.kayangan.absencehrd.Helper.AppController;
import com.example.kayangan.absencehrd.Helper.Constants;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Helper.SynchronizeData;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;

import org.apache.commons.net.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;

public class LoginActivity extends AppCompatActivity {
    AttendanceRecord data;

    Time time;

    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    SessionManager session;

    AlertDialogManager alertDialogManager = new AlertDialogManager();

    Button login, link;
    EditText name, password;

    ProgressDialog dialog;

    DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //bg orientation
        LinearLayout layout = findViewById(R.id.loginlayout);
        Resources resources = getResources();
        Drawable portrait = resources.getDrawable(R.drawable.bg_login);
        Drawable landscape = resources.getDrawable(R.drawable.bg_loginlans);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        handler = new DatabaseHandler(this);

        int num = display.getRotation();

        if (num == 0){
            layout.setBackgroundDrawable(portrait);
        }else if (num == 1 || num == 3){
            layout.setBackgroundDrawable(landscape);
        }else{
            layout.setBackgroundDrawable(portrait);
        }


        time = new Time();

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn())
            redirectToMainMenu();

        data = new AttendanceRecord();

        name = findViewById(R.id.txtName);
        password = findViewById(R.id.txtPassword);

        link = findViewById(R.id.btnLinkToRegisterScreen);
        login = findViewById(R.id.btnSignin);

        helper = new DatabaseHandler(this);
        db = helper.getReadableDatabase();

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Loading Process ...");
                        dialog.setTitle("LOGIN PROCESS");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                        String pass = password.getText().toString();
                        String nama = name.getText().toString();

                        if (nama.trim().length() > 0 && pass.trim().length() > 0){
                            dialog.setCancelable(false);
                            dialog.show();
                            LoginService(pass, nama);
                        }
                        else
                            alertDialogManager.showAlertDialog(LoginActivity.this, "LOGIN FAILED", "Please Enter Name and Password", false);
                    }
                }
        );

        link.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );


    }

    private void LoginService(final String password, final String nama){

        String url = Constants.url+"employees?A="+ Encode(nama);

        byte[] salt = new byte[0];
        try {
            salt = Constants.getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        final String pass = Constants.encryptPassword(password, salt);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (pass.equals(response.getString("password").toString())  )
                            {
                                Toast.makeText(LoginActivity.this, "LOGIN SUKSES!", Toast.LENGTH_SHORT).show();

                                String currentUserName = response.getString("name");
                                String currentID = response.getString("employeeID");

                                //tanya mengenai zone saat briefing
                                String currentZONE = "A";

                                Constants.currentUserID = currentID;

                                session.createLoginSession(currentUserName, currentID, currentZONE);

                                dialog.dismiss();

                                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                SynchronizeData.getInstance(LoginActivity.this).SyncLocation();

                                //jika tabel user sqlite tidak mempunyai isi
                                if (handler.isTableExists())
                                    SynchronizeData.getInstance(LoginActivity.this).syncUsers();

                                finish();
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "PASSWORD SALAH", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "TRY AGAIN", Toast.LENGTH_SHORT).show();
                        Log.e("AAA", ""+error.toString());
                        error.printStackTrace();
                    }
                });


        AppController.getInstance(LoginActivity.this).addToRequestque(objectRequest);
    }

    public String Encode(String text)
    {
        byte[] ptext = Base64.encodeBase64(text.getBytes());
        String a = new String(ptext);
        return a;
    }

    public String getDate(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);

        return dateString;
    }

    private void redirectToMainMenu(){
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        finish();
    }
}
