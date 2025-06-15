package com.example.drmeet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drmeet.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DrMeet.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_DOCTORS = "doctors";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SPECIALITY = "speciality";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_IMAGE = "image";

    public static final String PATIENT_INFORMATION_TABLE = "patient_information";
    public static final String PATIENT_INFORMATION_ID = "id";
    public static final String PATIENT_INFORMATION_NAME = "name";
    public static final String PATIENT_INFORMATION_EMAIL = "email";
    public static final String PATIENT_INFORMATION_PHONE = "phone";
    public static final String PATIENT_APPOIMENT_TYPE = "appoimentType";
    public static final String PATIENT_APPOIMENT_TIME = "slotTime";
    public static final String SLOTE_TABLE = "time_table";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_DOCTORS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_SPECIALITY + " TEXT, "
                + COLUMN_RATING + " REAL, "
                + COLUMN_IMAGE + " BLOB)";
        db.execSQL(CREATE_DOCTOR_TABLE);

        String CREATE_PATIENT_INFORMATION_TABLE = "CREATE TABLE " + PATIENT_INFORMATION_TABLE + " ("
                + PATIENT_INFORMATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PATIENT_INFORMATION_NAME + " TEXT, "
                + PATIENT_INFORMATION_EMAIL + " TEXT, "
                + PATIENT_APPOIMENT_TYPE + " TEXT, "
                + PATIENT_APPOIMENT_TIME + " TEXT, "
                + PATIENT_INFORMATION_PHONE + " TEXT)";
        db.execSQL(CREATE_PATIENT_INFORMATION_TABLE);

        String CREATE_SLOTE_TABLE = "CREATE TABLE " + SLOTE_TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PATIENT_APPOIMENT_TIME + " TEXT)";
        db.execSQL(CREATE_SLOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + PATIENT_INFORMATION_TABLE);
        onCreate(db);
    }

    public boolean insertDoctor(String name, String speciality, float rating, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SPECIALITY, speciality);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_IMAGE, image);

        long result = db.insert(TABLE_DOCTORS, null, values);
        return result != -1;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String speciality = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIALITY));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

                Doctor doctor = new Doctor(id, name, speciality, rating, image);
                doctorList.add(doctor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return doctorList;
    }

    public Doctor getDoctorByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS, null, COLUMN_NAME + " = ?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String speciality = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIALITY));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

            Doctor doctor = new Doctor(id, name, speciality, rating, image);
            cursor.close();
            return doctor;
        }
        return null;
    }

    public boolean deleteDoctor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_DOCTORS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean updateDoctor(int id, String name, String speciality, float rating, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SPECIALITY, speciality);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_IMAGE, image);

        int result = db.update(TABLE_DOCTORS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getAllPatient() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PATIENT_INFORMATION_TABLE, null);
    }

    public void addPatient(String name, String email, String phone, String appoimentType, String slotTime) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();

        values1.put(PATIENT_INFORMATION_NAME, name);
        values1.put(PATIENT_INFORMATION_EMAIL, email);
        values1.put(PATIENT_INFORMATION_PHONE, phone);
        values1.put(PATIENT_APPOIMENT_TYPE, appoimentType);
        values1.put(PATIENT_APPOIMENT_TIME, slotTime);
        db1.insert(PATIENT_INFORMATION_TABLE, null, values1);
        db1.close();
    }

    public void insertTime(String time){
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues values2 = new ContentValues();
        values2.put(PATIENT_APPOIMENT_TIME, time);
        db2.insert(SLOTE_TABLE, null, values2);
        db2.close();
    }

    public boolean isTimeAvailable(String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SLOTE_TABLE, null, PATIENT_APPOIMENT_TIME + " = ?", new String[]{time}, null, null, null);
        boolean isAvailable = cursor.getCount() == 0;
        cursor.close();
        return isAvailable;
    }
}


//package com.example.drmeet;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "DrMeet.db";
//    private static final int DATABASE_VERSION = 2;
//
//    public static final String TABLE_DOCTORS = "doctors";
//    public static final String COLUMN_ID = "id";
//    public static final String COLUMN_NAME = "name";
//    public static final String COLUMN_SPECIALITY = "speciality";
//    public static final String COLUMN_RATING = "rating";
//    public static final String COLUMN_IMAGE = "image";
//
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_DOCTORS + " ("
//                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COLUMN_NAME + " TEXT, "
//                + COLUMN_SPECIALITY + " TEXT, "
//                + COLUMN_RATING + " REAL, "
//                + COLUMN_IMAGE + " BLOB)";
//        db.execSQL(CREATE_DOCTOR_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
//        onCreate(db);
//    }
//
//    public boolean insertDoctor(String name, String speciality, float rating, byte[] image) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_NAME, name);
//        values.put(COLUMN_SPECIALITY, speciality);
//        values.put(COLUMN_RATING, rating);
//        values.put(COLUMN_IMAGE, image);
//
//        long result = db.insert(TABLE_DOCTORS, null, values);
//        return result != -1;
//    }
//
//    public List<Doctor> getAllDoctors() {
//        List<Doctor> doctorList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_DOCTORS, null, null, null, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
//                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
//                String speciality = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIALITY));
//                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING));
//                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
//
//                Doctor doctor = new Doctor(id, name, speciality, rating, image);
//                doctorList.add(doctor);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//        return doctorList;
//    }
//
//    public Doctor getDoctorByName(String name) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_DOCTORS, null, COLUMN_NAME + " = ?", new String[]{name}, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
//            String speciality = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIALITY));
//            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING));
//            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
//
//            Doctor doctor = new Doctor(id, name, speciality, rating, image);
//            cursor.close();
//            return doctor;
//        }
//        return null;
//    }
//
//    public boolean deleteDoctor(int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int result = db.delete(TABLE_DOCTORS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
//        return result > 0;
//    }
//
//    public boolean updateDoctor(int id, String name, String speciality, float rating, byte[] image) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_NAME, name);
//        values.put(COLUMN_SPECIALITY, speciality);
//        values.put(COLUMN_RATING, rating);
//        values.put(COLUMN_IMAGE, image);
//
//        int result = db.update(TABLE_DOCTORS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
//        return result > 0;
//    }
//}
