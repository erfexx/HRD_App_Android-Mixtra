package com.example.kayangan.absencehrd.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.AlertDialogManager;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.User;
import com.example.kayangan.absencehrd.R;

public class RegistrationActivity extends AppCompatActivity {
    SQLiteOpenHelper helper;
    SQLiteDatabase database;

    Button regis, link;
    EditText nama, pass;

    Cursor cursor;

    AlertDialogManager alertDialogManager = new AlertDialogManager();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        helper = new DatabaseHandler(this);

        nama = findViewById(R.id.txtName);
        pass = findViewById(R.id.txtPassword);

        link = findViewById(R.id.btnLinkToLoginScreen);
        regis = findViewById(R.id.btnDaftar);

        regis.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        database = helper.getWritableDatabase();

                        dialog = new ProgressDialog(RegistrationActivity.this);
                        dialog.setMessage("Loading Registration Process ...");
                        dialog.setTitle("REGISTRATION PROCESS");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                        String name = nama.getText().toString();
                        String password = pass.getText().toString();

                        if (name.trim().length() > 0 && password.trim().length() > 0){
                            User user = new User();
                            user.setName(name);
                            user.setPassword(password);

                            if (checkData())
                            {
                                insertData(user);

                                dialog.setCancelable(false);
                                dialog.show();

                                new Thread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(10000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                ).start();

                                startActivity(intent);
                                Toast.makeText(RegistrationActivity.this, "REGISTRATION COMPLETED!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this, "THE NAME IS ALREADY REGISTERED!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            alertDialogManager.showAlertDialog(RegistrationActivity.this, "REGISTRATION FAILED", "Please Enter Name or Password", false);
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

    private void insertData(User user){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_NAME, user.getName());
        values.put(DatabaseHandler.KEY_PASS, user.getPassword());

        long id = database.insert(DatabaseHandler.TABLE_USERS, null, values);
    }

    private boolean checkData(){
        database = helper.getWritableDatabase();

        nama = findViewById(R.id.txtName);
        String name = nama.getText().toString();

        cursor = database.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_USERS + " WHERE " + DatabaseHandler.KEY_NAME + " = ?"
                                    , new String[]{name});
        if (cursor != null)
            if (cursor.getCount() <= 0)
            {
                return true;
            }

        return false;
    }


}
