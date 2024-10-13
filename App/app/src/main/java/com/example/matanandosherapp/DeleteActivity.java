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

public class DeleteActivity extends AppCompatActivity {

    EditText etIdFunc;
    Button btnSubmitId, btnBack, btnYes, btnNo;
    TextView tvTheFunc, tvQuestion, tvProperty;
    String idStr;
    int idInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        DatabaseHelper db = MainActivity.db;
        etIdFunc = findViewById(R.id.etIdFunc);
        tvTheFunc = findViewById(R.id.tvTheFunc);
        tvProperty = findViewById(R.id.tvProperty);
        btnSubmitId = findViewById(R.id.btnSubmitId);
        btnBack = findViewById(R.id.btnBack);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        tvQuestion = findViewById(R.id.tvQuestion);

        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (etIdFunc.getText().toString().isEmpty()){
                    Toast.makeText(DeleteActivity.this, "Please enter an id.", Toast.LENGTH_SHORT).show();
                }
                else {
                    idStr = etIdFunc.getText().toString().trim();
                    idInt = Integer.parseInt(idStr);
                    Cursor res = db.getPoly(idInt);
                    if (res == null || !res.moveToFirst() || idStr.charAt(0) == '0') {
                        Toast.makeText(DeleteActivity.this, "No polynomial found for ID " + idStr, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        boolean allCoefficientsZero = true;
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
                                    buffer.append("X<sup>").append(degree).append("</sup>"); // append X^degree
                                }
                            }
                        } while (res.moveToNext());

                        if (allCoefficientsZero) {
                            tvProperty.setText("The function f" + MainActivity.getSubscript(idStr) + "(x) is: 0");
                        } else {
                            if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == '+') {
                                buffer.deleteCharAt(buffer.length() - 1);
                            }
                            tvTheFunc.setText(Html.fromHtml(buffer.toString()));
                            tvProperty.setText("The function f" + MainActivity.getSubscript(idStr) + "(x) is:");
                        }
                        tvProperty.setVisibility(View.VISIBLE);
                        tvTheFunc.setVisibility(View.VISIBLE);
                        tvQuestion.setVisibility(View.VISIBLE);
                        btnNo.setVisibility(View.VISIBLE);
                        btnYes.setVisibility(View.VISIBLE);
                        btnSubmitId.setVisibility(View.INVISIBLE);
                    }
                }
                etIdFunc.setText("");
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTheFunc.setVisibility(View.INVISIBLE);
                tvTheFunc.setText("");
                tvQuestion.setVisibility(View.INVISIBLE);
                btnNo.setVisibility(View.INVISIBLE);
                btnYes.setVisibility(View.INVISIBLE);
                tvProperty.setVisibility(View.INVISIBLE);
                btnSubmitId.setVisibility(View.VISIBLE);
                tvProperty.setText("");
                db.deleteFunction(idStr); // Use idInt instead of idStr
                MainActivity.id = 0;
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTheFunc.setVisibility(View.INVISIBLE);
                tvTheFunc.setText("");
                tvProperty.setText("");
                tvProperty.setVisibility(View.INVISIBLE);
                tvQuestion.setVisibility(View.INVISIBLE);
                btnNo.setVisibility(View.INVISIBLE);
                btnYes.setVisibility(View.INVISIBLE);
                btnSubmitId.setVisibility(View.VISIBLE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteActivity.this.finish();
            }
        });
    }
}
