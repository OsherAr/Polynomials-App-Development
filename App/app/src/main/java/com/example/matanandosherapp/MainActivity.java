package com.example.matanandosherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnSearch, btnDelete, btnUpdate, btnShowData;
    static DatabaseHelper db;
    static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        while (true) {
            ++id;
            SQLiteDatabase sld = db.getReadableDatabase();
            Cursor cursor = sld.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE id_function = " + id, null);
            if (cursor.getCount() == 0) break;
        }

        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnShowData = findViewById(R.id.btnShowData);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.matanandosherapp.AddActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.matanandosherapp.SearchActivity.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.matanandosherapp.DeleteActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.matanandosherapp.UpdateActivity.class);
                startActivity(intent);
            }
        });

        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.matanandosherapp.ShowDataActivity.class);
                startActivity(intent);
            }
        });



    }

    // converting digits in a string to their subscript unicode equivalents.
    public static String getSubscript(String idStr) {
        StringBuilder subscript = new StringBuilder();
        for (char c : idStr.toCharArray()) {
            switch (c) {
                case '0':
                    subscript.append("₀");
                    break;
                case '1':
                    subscript.append("₁");
                    break;
                case '2':
                    subscript.append("₂");
                    break;
                case '3':
                    subscript.append("₃");
                    break;
                case '4':
                    subscript.append("₄");
                    break;
                case '5':
                    subscript.append("₅");
                    break;
                case '6':
                    subscript.append("₆");
                    break;
                case '7':
                    subscript.append("₇");
                    break;
                case '8':
                    subscript.append("₈");
                    break;
                case '9':
                    subscript.append("₉");
                    break;
            }
        }
        return subscript.toString();
    }
}
