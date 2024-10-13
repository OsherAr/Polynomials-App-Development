package com.example.matanandosherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText etIdFunc, etUpdateCoef, etUpdateDegree;
    Button btnSubmitId, btnBack, btnSubmitUpdate;
    TextView tvDeg, tvCoef, tvChooseNumFunc;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db = MainActivity.db;

        etIdFunc = findViewById(R.id.etIdFunc);
        btnSubmitId = findViewById(R.id.btnSubmitId);
        tvDeg = findViewById(R.id.tvDeg);
        tvCoef = findViewById(R.id.tvCoef);
        btnSubmitUpdate = findViewById(R.id.btnSubmitUpdate);
        etUpdateCoef = findViewById(R.id.etUpdateCoef);
        etUpdateDegree = findViewById(R.id.etUpdateDegree);
        btnBack = findViewById(R.id.btnBack);
        tvChooseNumFunc = findViewById(R.id.tvChooseNumFunc);

        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                etIdFunc.setVisibility(View.GONE);
                btnSubmitId.setVisibility(View.GONE);
                String idStr = etIdFunc.getText().toString().trim();
                if (idStr.isEmpty()) {

                    Toast.makeText(UpdateActivity.this, "Please enter an ID.", Toast.LENGTH_SHORT).show();
                } else {
                    idStr = etIdFunc.getText().toString().trim();
                    int idInt = Integer.parseInt(idStr);
                    Cursor res = db.getPoly(idInt);
                    if (res == null || !res.moveToFirst()) {
                        Toast.makeText(UpdateActivity.this, "No polynomial found for ID " + idStr, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        tvDeg.setVisibility(View.VISIBLE);
                        tvCoef.setVisibility(View.VISIBLE);
                        etUpdateCoef.setVisibility(View.VISIBLE);
                        etUpdateDegree.setVisibility(View.VISIBLE);
                        btnSubmitUpdate.setVisibility(View.VISIBLE);
                        tvChooseNumFunc.setText("Update Function number " + idStr);
                    }
                }
            }
        });

        btnSubmitUpdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String idStr = etIdFunc.getText().toString().trim();
                String deg = etUpdateDegree.getText().toString().trim();
                String coef = etUpdateCoef.getText().toString().trim();


                if (deg.isEmpty() || coef.isEmpty()) {
                    Toast.makeText(UpdateActivity.this, "Please enter degree and coefficient.", Toast.LENGTH_SHORT).show();
                } else {
                    int rowsUpdated = db.updatePolynomial(idStr, coef, deg);

                    if (rowsUpdated > 0) {
                        Toast.makeText(UpdateActivity.this, "Polynomial updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }
                etIdFunc.setVisibility(View.VISIBLE);
                etIdFunc.setText("");
                btnSubmitId.setVisibility(View.VISIBLE);
                tvChooseNumFunc.setText("Search function number by the id:");
                etUpdateCoef.setText("");
                etUpdateCoef.setVisibility(View.GONE);
                etUpdateDegree.setText("");
                etUpdateDegree.setVisibility(View.GONE);
                btnSubmitUpdate.setVisibility(View.GONE);
                tvCoef.setVisibility(View.GONE);
                tvDeg.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // go back to the MainActivity
            }
        });
    }
}
