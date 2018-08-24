package com.mit.mobile.absencehrd.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mit.mobile.absencehrd.Activity.MenuActivity;
import com.mit.mobile.absencehrd.Helper.AppController;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.SynchronizeData;
import com.mit.mobile.absencehrd.Model.AttendanceRecord;
import com.mit.mobile.absencehrd.R;
import com.mit.mobile.absencehrd.Helper.SessionManager;

import org.apache.commons.net.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;

public class LoginFragment extends Fragment {
    AttendanceRecord data;

    Time time;

    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    SessionManager session;

    AlertDialog.Builder builder;

    Button login, link;
    EditText name, password;

    ProgressDialog dialog;

    DatabaseHandler handler;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        handler = new DatabaseHandler(getActivity());

        builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("LOGIN FAILED");
        builder.setMessage("Please Enter Name and Password!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        time = new Time();

        session = new SessionManager(getActivity());

        if (session.isLoggedIn())
            redirectToMainMenu();

        data = new AttendanceRecord();

        name = view.findViewById(R.id.txtName);
        password = view.findViewById(R.id.txtPassword);

        //link = findViewById(R.id.btnLinkToRegisterScreen);
        login = view.findViewById(R.id.btnSignin);

        helper = new DatabaseHandler(getActivity());
        db = helper.getReadableDatabase();

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new ProgressDialog(getActivity());
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
                        {
                            builder.create();
                            builder.show();
                        }
                        //alertDialogManager.showAlertDialog(LoginFragment.this, "LOGIN FAILED", "Please Enter Name and Password", false);
                    }
                }
        );

        return view;
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
                                Toast.makeText(getActivity(), "LOGIN SUKSES!", Toast.LENGTH_SHORT).show();

                                String currentUserName = response.getString("name");
                                String currentID = response.getString("employeeID");
                                String currentGENDER = response.getString("gender");

                                Log.i("LOGGONG", ""+currentGENDER);

                                //tanya mengenai zone saat briefing
                                String currentZONE = "A";

                                Constants.currentUserID = currentID;

                                session.createLoginSession(currentUserName, currentID, currentZONE, currentGENDER);

                                dialog.dismiss();

                                startActivity(new Intent(getActivity(), MenuActivity.class));
                                SynchronizeData.getInstance(getActivity()).SyncLocation();

                                //jika tabel user sqlite tidak mempunyai isi
                                if (handler.isTableExists())
                                    SynchronizeData.getInstance(getActivity()).syncUsers();

                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "PASSWORD SALAH", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "TRY AGAIN", Toast.LENGTH_SHORT).show();
                        Log.e("AAA", ""+error.toString());
                        error.printStackTrace();
                    }
                });


        AppController.getInstance(getActivity()).addToRequestque(objectRequest);
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
        startActivity(new Intent(getActivity(), MenuActivity.class));

    }
}
