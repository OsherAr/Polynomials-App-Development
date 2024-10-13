package com.example.matanandosherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.Comparator;

public class ShowDataActivity extends AppCompatActivity {

    ListView listView;
    Button btnBack;
    List<String> itemList; // to hold items for ListView
    ArrayAdapter<String> adapter; // ArrayAdapter to ListView

    @Override
    @SuppressLint("Range")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        btnBack = findViewById(R.id.btnBack2);
        listView = findViewById(R.id.listView);
        itemList = new ArrayList<>();
        Cursor res = MainActivity.db.getAllPoly();

        if (res != null && res.moveToFirst()) {
            // map to store data by ID
            TreeMap<String, StringBuilder> idToDataMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String id1, String id2) {
                    int idValue1 = Integer.parseInt(id1);
                    int idValue2 = Integer.parseInt(id2);
                    return Integer.compare(idValue1, idValue2);
                }
            });

            int counter=0;
            int idtemp=Integer.parseInt(res.getString(res.getColumnIndex(DatabaseHelper.KEY_ID)));


            do {
                String id = res.getString(res.getColumnIndex(DatabaseHelper.KEY_ID));
                String coefficient = res.getString(res.getColumnIndex(DatabaseHelper.KEY_COEFFICIENT));
                String degree = res.getString(res.getColumnIndex(DatabaseHelper.KEY_DEGREE));

                if(idtemp!=Integer.parseInt(id)){
                    counter=0;
                    idtemp=Integer.parseInt(id);
                }

                if (coefficient.equals("0")) {
                    counter++;
                    Cursor checkLastDegree = MainActivity.db.getPoly(Integer.parseInt(id));
                    checkLastDegree.moveToLast();

                    int polyDegree = Integer.parseInt(checkLastDegree.getString(checkLastDegree.getColumnIndex(DatabaseHelper.KEY_DEGREE)));
                    if(counter==polyDegree+1){
                        if (!idToDataMap.containsKey(id)) {
                            idToDataMap.put(id, new StringBuilder("0"));
                        }
                    }
                    continue;
                }

                StringBuilder rowData = new StringBuilder();

                if (!coefficient.startsWith("-") && idToDataMap.containsKey(id) && Objects.requireNonNull(idToDataMap.get(id)).length() > 0) {
                    rowData.append(" + ");
                }
                rowData.append(coefficient);

                if (!degree.equals("0")) {
                    rowData.append("X^").append(degree);
                }

                if (!idToDataMap.containsKey(id)) {
                    idToDataMap.put(id, new StringBuilder());
                }
                Objects.requireNonNull(idToDataMap.get(id)).append(rowData).append(" ");

            } while (res.moveToNext());

            for (Map.Entry<String, StringBuilder> entry : idToDataMap.entrySet()) {
                String id = entry.getKey();
                String dataForId = entry.getValue().toString();

                String listItem = "ID: " + id + "\n" + dataForId;
                itemList.add(listItem);
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);

        listView.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDataActivity.this.finish();
            }
        });
    }
}
