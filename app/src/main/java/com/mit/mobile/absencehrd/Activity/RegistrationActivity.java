package com.mit.mobile.absencehrd.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mit.mobile.absencehrd.Helper.AlertDialogManager;
import com.mit.mobile.absencehrd.Helper.AppController;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.SessionManager;
import com.mit.mobile.absencehrd.Model.User;
import com.mit.mobile.absencehrd.R;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import static com.mit.mobile.absencehrd.Helper.Constants.encryptPassword;

public class RegistrationActivity extends AppCompatActivity {
    SQLiteOpenHelper helper;
    SQLiteDatabase database;

    Button regis, link;
    EditText nama, pass, ID;

    Cursor cursor;

    AlertDialogManager alertDialogManager = new AlertDialogManager();

    SessionManager sessionManager;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //bg orientation
        LinearLayout layout = findViewById(R.id.registLayout);
        Resources resources = getResources();
        Drawable portrait = resources.getDrawable(R.drawable.bg_login);
        Drawable landscape = resources.getDrawable(R.drawable.bg_loginlans);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        int num = display.getRotation();

        if (num == 0){
            layout.setBackgroundDrawable(portrait);
        }else if (num == 1 || num == 3){
            layout.setBackgroundDrawable(landscape);
        }else{
            layout.setBackgroundDrawable(portrait);
        }


        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn())
            redirectToMainMenu();


        helper = new DatabaseHandler(this);

        nama = findViewById(R.id.txtName);
        pass = findViewById(R.id.txtPassword);
        //ID = findViewById(R.id.txtID);

        link = findViewById(R.id.btnLinkToLoginScreen);
        regis = findViewById(R.id.btnDaftar);

        regis.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new ProgressDialog(RegistrationActivity.this);
                        dialog.setMessage("Loading Registration Process ...");
                        dialog.setTitle("REGISTRATION PROCESS");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                        String name = nama.getText().toString();
                        byte[] salt = new byte[0];
                        try {
                            salt = Constants.getSalt();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        }
                        String password = encryptPassword(pass.getText().toString(), salt);

                        if (name.trim().length() > 0 && password.trim().length() > 0){
                            dialog.setCancelable(false);
                            dialog.show();

                            RegistrationService(name, password);
                        }


                    }
                }
        );

        link.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }


    /*KOKOKOKKOKOK

    private void insertData(User user){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_NAME, user.getName());
        values.put(DatabaseHandler.KEY_PASS, user.getPassword());
        values.put(DatabaseHandler.KEY_ZONE, user.getZone());

        long id = database.insert(DatabaseHandler.TABLE_USERS, null, values);
    }

    KOKOKOKOK
*/
    private boolean checkData(){
        database = helper.getWritableDatabase();

        nama = findViewById(R.id.txtName);
        String name = nama.getText().toString();

        cursor = database.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_EMPLOYEES + " WHERE " + DatabaseHandler.EMP_NAME + " = ?"
                , new String[]{name});
        if (cursor != null)
            if (cursor.getCount() <= 0)
            {
                return true;
            }

        return false;
    }

    private void redirectToMainMenu(){
        startActivity(new Intent(RegistrationActivity.this, MenuActivity.class));
        finish();
    }

    private void RegistrationService(final String name, final String password){
        String url = Constants.url+"users";

        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        Toast.makeText(RegistrationActivity.this, "REGISTRATION COMPLETE", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.e("AAA", error.toString());
                        Toast.makeText(RegistrationActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id",""+ID);
                params.put("name",""+name);
                params.put("password",""+password);
                params.put("zone","A");
                params.put("created_at","");
                params.put("updated_at","");
                params.put("attendance","");

                return params;
            }
        };

        AppController.getInstance(RegistrationActivity.this).addToRequestque(objectRequest);
    }
}
