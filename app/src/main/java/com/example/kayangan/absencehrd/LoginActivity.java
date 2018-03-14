package com.example.kayangan.absencehrd;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.Model.UserModel;

import java.sql.Time;
import java.text.SimpleDateFormat;

public class LoginActivity extends AppCompatActivity {

    android.text.format.Time time;

    Handler handler;
    Runnable runnable;

    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor, searchID;

    Button login;
    EditText name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.txtName);
        password = findViewById(R.id.txtPassword);
        login = findViewById(R.id.btnSignin);

        helper = new DatabaseHandler(this);
        db = helper.getReadableDatabase();

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserModel user = new UserModel();


                        String nama = name.getText().toString();
                        String pass = password.getText().toString();

                        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_USERS +
                                " WHERE " + DatabaseHandler.KEY_NAME+ "=? AND " + DatabaseHandler.KEY_PASS +
                                "=?", new String[]{nama, pass});


                        if (cursor!=null)
                        {
                            if (cursor.getCount() > 0)
                            {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Toast.makeText(LoginActivity.this, "LOGIN SUKSES", Toast.LENGTH_SHORT).show();

                                cursor.moveToFirst();


                                String currentUserName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)).toString();
                                user.setName(currentUserName);

                                String currentID = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)).toString();

                                currentUser.currentUserID = currentID;
                                currentUser.currentUser = user;

                                AttendanceRecord data = new AttendanceRecord();
                                data.setUser_id(currentID);
                                data.setDate("");
                                data.setFlag("0");

                                setAwal(data);

                                startActivity(intent);
                                finish();
                            }
                            else
                                Toast.makeText(LoginActivity.this, "LOGIN GAGAL", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


    }

    /*public String getDate(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);

        return dateString;
    }*/

    public void setAwal(AttendanceRecord data){
        /*ContentValues values = new ContentValues();

        values.put(DatabaseHandler.ATT_USER_ID, data.getUser_id());
        values.put(DatabaseHandler.ATT_DATE, data.getDate());
        values.put(DatabaseHandler.ATT_FLAG_TAP, data.getFlag());

        db.insert(DatabaseHandler.TABLE_ATTENDANCES, null, values);*/
    }
}
