package com.example.kayangan.absencehrd.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.AlertDialogManager;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;
import com.example.kayangan.absencehrd.Helper.currentUser;

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


                        String nama = name.getText().toString();
                        String pass = password.getText().toString();

                        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_USERS +
                                " WHERE " + DatabaseHandler.KEY_NAME+ "=? AND " + DatabaseHandler.KEY_PASS +
                                "=?", new String[]{nama, pass});

                        if (nama.trim().length() > 0 && pass.trim().length() > 0){
                            if (cursor!=null)
                            {
                                if (cursor.getCount() > 0)
                                {
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

                                    cursor.moveToFirst();

                                    String currentUserName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME));
                                    String currentID = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID));

                                    currentUser.currentUserID = currentID;

                                    session.createLoginSession(currentUserName, currentID);

                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "LOGIN SUKSES", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                    Toast.makeText(LoginActivity.this, "WRONG NAME OR PASSWORD", Toast.LENGTH_SHORT).show();
                            }
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
