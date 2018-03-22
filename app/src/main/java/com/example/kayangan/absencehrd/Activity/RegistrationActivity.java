package com.example.kayangan.absencehrd.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayangan.absencehrd.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.User;
import com.example.kayangan.absencehrd.R;

public class RegistrationActivity extends AppCompatActivity {
    SQLiteOpenHelper helper;
    SQLiteDatabase database;

    Button regis;
    EditText nama, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        helper = new DatabaseHandler(this);

        nama = findViewById(R.id.txtName);
        pass = findViewById(R.id.txtPassword);
        regis = findViewById(R.id.btnDaftar);

        regis.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        database = helper.getWritableDatabase();

                        String name = nama.getText().toString();
                        String password = pass.getText().toString();

                        User user = new User();
                        user.setName(name);
                        user.setPassword(password);

                        insertData(user);
                        Toast.makeText(RegistrationActivity.this, "REGISTRASI BERHASIL", Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                        finish();
                    }
                }
        );
    }

    public void insertData(User user){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.KEY_NAME, user.getName());
        values.put(DatabaseHandler.KEY_PASS, user.getPassword());

        long id = database.insert(DatabaseHandler.TABLE_USERS, null, values);
    }
}
