package com.example.matanandosherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    Button btnAddCoefficient , btnSub, btnDone;
    EditText etExp, etCoefficient;
    TextView tvExp, tvDegree;
    String maxExponent, coefficient;
    int maxExpInt;
    int counterToMaxExponent = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnSub = findViewById(R.id.btnSub);
        btnAddCoefficient = findViewById(R.id.btnAddCoefficient);
        btnAddCoefficient.setVisibility(View.INVISIBLE);
        btnDone = findViewById(R.id.btnDone);

        etExp = findViewById(R.id.etExp);
        etCoefficient = findViewById(R.id.etCoefficient);
        etCoefficient.setVisibility(View.INVISIBLE);

        tvDegree = findViewById(R.id.tvDegree);

        tvExp = findViewById(R.id.tvExp);
        tvExp.setVisibility(View.INVISIBLE);
        tvExp.setText("Enter a coefficient for exponent: " + counterToMaxExponent);


        btnSub.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n") // to prevent the warnings on line 49
            @Override
            public void onClick(View view) {
                MainActivity.id=0;
                while (true) {
                    ++MainActivity.id;
                    SQLiteDatabase sld = MainActivity.db.getReadableDatabase();
                    Cursor cursor = sld.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE id_function = " + MainActivity.id, null);
                    if (cursor.getCount() == 0) break;
                }
                if(etExp.getText().toString().isEmpty()){
                    Toast.makeText(AddActivity.this, "Please enter an exponent for your polynomial.", Toast.LENGTH_SHORT).show();
                }
                else{
                    maxExponent = etExp.getText().toString().trim();

                    maxExpInt = Integer.parseInt(maxExponent);
                    tvDegree.setText("The polynomial degree is: " + maxExpInt);
                    etExp.setVisibility(View.GONE);
                    btnAddCoefficient.setVisibility(View.VISIBLE);
                    etCoefficient.setVisibility(View.VISIBLE);

                    tvExp.setVisibility(View.VISIBLE);
                    btnSub.setVisibility(View.GONE);



                    btnAddCoefficient.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            coefficient = etCoefficient.getText().toString().trim();
                            if (maxExpInt >= 0) {
                                if(coefficient.isEmpty()) {
                                    Toast.makeText(AddActivity.this, "Please enter a coefficient for your polynomial.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(maxExpInt == 0){
                                        counterToMaxExponent++; // Increment counter first
                                        maxExpInt--;
                                        MainActivity.db.insertPolynomial(String.valueOf(MainActivity.id),coefficient,counterToMaxExponent-1);
                                        Toast.makeText(getApplicationContext(),"Added Successful",Toast.LENGTH_LONG).show();
                                        etCoefficient.setText("");
                                        etCoefficient.setVisibility(View.INVISIBLE);
                                        btnAddCoefficient.setVisibility(View.INVISIBLE);
                                        MainActivity.id++;
                                        tvExp.setText("The polynomial is added");
                                        etCoefficient.setVisibility(View.INVISIBLE);
                                        btnAddCoefficient.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        counterToMaxExponent++; // Increment counter first
                                        tvExp.setText("Enter a coefficient for exponent: " + counterToMaxExponent);
                                        maxExpInt--;
                                        MainActivity.db.insertPolynomial(String.valueOf(MainActivity.id),coefficient,counterToMaxExponent-1);
                                        Toast.makeText(getApplicationContext(),"Added Successful",Toast.LENGTH_LONG).show();
                                        etCoefficient.setText("");
                                        etCoefficient.setHint("Enter coefficient");
                                    }
                                }
                            }
                            else{
                                tvExp.setText("The polynomial is added");
                                etCoefficient.setVisibility(View.INVISIBLE);
                                btnAddCoefficient.setVisibility(View.INVISIBLE);
                                MainActivity.id++;
                                MainActivity.db.orderTableByMultipleColumns();
                            }
                        }
                    });

                }

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.finish();
            }
        });


    }
}