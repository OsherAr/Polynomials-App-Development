package com.example.matanandosherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    EditText etIdFunc;
    Button btnSubmitId, btnBack;
    TextView tvTheFunc,tvPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final  com.example.matanandosherapp.DatabaseHelper db = MainActivity.db;
        etIdFunc = findViewById(R.id.etIdFunc);
        tvTheFunc = findViewById(R.id.tvTheFunc);
        tvPresent = findViewById(R.id.tvPresent);
        btnSubmitId = findViewById(R.id.btnSubmitId);
        btnBack = findViewById(R.id.btnBack);

        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (etIdFunc.getText().toString().isEmpty()){
                    Toast.makeText(SearchActivity.this, "Please enter an id.", Toast.LENGTH_SHORT).show();
                }
                else {
                    tvTheFunc.setText("");
                    tvPresent.setText("");
                    String idStr = etIdFunc.getText().toString().trim();
                    int idInt = Integer.parseInt(idStr);
                    if (idStr.charAt(0) == '0') {
                        Toast.makeText(SearchActivity.this, "No polynomial found for ID " + idStr, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        boolean allCoefficientsZero = true;
                        Cursor res = db.getPoly(idInt);

                        if (res != null && res.moveToFirst()) {
                            StringBuilder buffer = new StringBuilder();

                            do {
                                @SuppressLint("Range") String coefficient = res.getString(res.getColumnIndex(DatabaseHelper.KEY_COEFFICIENT));
                                @SuppressLint("Range") String degree = res.getString(res.getColumnIndex(DatabaseHelper.KEY_DEGREE));
                                if (!coefficient.equals("0")) {
                                    allCoefficientsZero = false;
                                    if (buffer.length() > 0 && !coefficient.startsWith("-")) {
                                        buffer.append("+");
                                    }

                                    buffer.append(coefficient);

                                    if (!degree.equals("0")) {
                                        buffer.append("X<sup>").append(degree).append("</sup>"); // Append X^degree

                                    }
                                }
                            } while (res.moveToNext());

                            if (allCoefficientsZero){
                                tvPresent.setText("The function f" + MainActivity.getSubscript(idStr) + "(x) is: 0");
                            }

                            else {

                                // remove '+' if present
                                if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == '+') {
                                    buffer.deleteCharAt(buffer.length() - 1);
                                }
                                tvPresent.setText("The function f" + MainActivity.getSubscript(idStr) + "(x) is:\n");
                                tvTheFunc.setText(Html.fromHtml(buffer.toString()));
                            }
                        } else {
                            Toast.makeText(SearchActivity.this, "No polynomial found for ID " + idStr, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

    }
}