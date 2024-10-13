package com.example.matanandosherapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "_polynomials";
    public static final String KEY_ID = "id_function";
    public static final String KEY_COEFFICIENT = "_coefficient";
    public static final String KEY_DEGREE = "_degree";
    public static final String DATABASE_NAME = "Polynomials.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME +
            " ("+KEY_ID+" INTEGER NOT NULL, " +
            KEY_COEFFICIENT +" DOUBLE NOT NULL, " +
            KEY_DEGREE+" INTEGER NOT NULL);";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPolynomial(String id, String coef, int degree) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID,id);
        contentValues.put(KEY_COEFFICIENT,coef);
        contentValues.put(KEY_DEGREE,degree);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public int updatePolynomial(String id, String coef, String degree) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COEFFICIENT, coef);
        String whereClause = KEY_ID + " = ? AND " + KEY_DEGREE + " = ?";
        String[] whereArgs = { id, degree };

        int rowsAffected = 0;

        try {
            rowsAffected = db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error updating Polynomial with ID " + id + " and degree " + degree, e);
        } finally {
            db.close();
        }

        return rowsAffected;
    }

    public void deleteFunction(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String whereClause = KEY_ID + " = ?";
            String[] whereArgs = { id };
            int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);
            if (rowsDeleted > 0) {
                Log.d(TAG, rowsDeleted + " row(s) deleted where id = " + id);
            } else {
                Log.d(TAG, "No rows deleted where id = " + id);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error deleting rows where id = " + id, e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
    public Cursor getPoly(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME +" WHERE " + KEY_ID + "= ? ", new String[] {String.valueOf(id) } );
        return res;
    }
    public Cursor getAllPoly() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void orderTableByMultipleColumns() {
        SQLiteDatabase db = this.getWritableDatabase();
        String orderBy = "id ASC, degree ASC";
        db.execSQL("CREATE TABLE temp_table AS SELECT * FROM " + TABLE_NAME + " ORDER BY " + orderBy);
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL("ALTER TABLE temp_table RENAME TO " + TABLE_NAME);
        db.close();
    }
}
