package com.example.fridgebuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MyDB extends SQLiteOpenHelper {

    private static MyDB sInstance;

    private Context context;
    private static String DB_NAME = "FOOD_DB";
    private static String TABLE_FOOD = "FOOD_TABLE";
    private static int VERSION = 1;

    private static String ID_FOOD = "FOOD_ID";
    private static String FOOD_NAME = "NAME_FOOD";
    private static String AMOUNT_STORED = "STORED_AMOUNT";
    private static String DATE_STORED = "STORED_DATE";


    public MyDB(Context context){
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_FOOD + "(" + ID_FOOD + " INTEGER PRIMARY KEY ," + FOOD_NAME + " TEXT," + AMOUNT_STORED + " TEXT," + DATE_STORED + " TEXT )";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (VERSION == oldVersion) {
            VERSION = newVersion;
            db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD + ";");
            onCreate(db);
        }
    }

    /**
     * Ensures only one MyDB will exist at any given time. if sInstance has not been initialized
     * one will be created. If one has already been created then it'll be returned.
     */
    public static synchronized MyDB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MyDB(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Inserts a food item into the db
     */
    public void addFood(String foodName, String amountStored){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        values.put(FOOD_NAME, foodName);
        values.put(AMOUNT_STORED, amountStored);
        values.put(DATE_STORED, date);

        db.insert(TABLE_FOOD, null, values);
        db.close();
    }

    /**
     * Returns all data in the db
     */
    public ArrayList<String> getAllFoods(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> foodList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String currentFood = cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3);
            foodList.add(currentFood);
            cursor.moveToNext();
        }
        cursor.close();
        return foodList;
    }

    /**
     * Return a String list of all ingredients in ArrayList
     */
    public ArrayList<String> getIngredListArr(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> foodList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String currentFood = cursor.getString(1);
            foodList.add(currentFood);
            cursor.moveToNext();
        }
        cursor.close();
        Collections.sort(foodList);
        return foodList;
    }

    /**
     * Return a String list of all ingredients separated by commas.
     */
    public String getIngredList(){
        SQLiteDatabase db = getReadableDatabase();
        String ingredList = "";
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ingredList = ingredList + cursor.getString(1) + ",";
            cursor.moveToNext();
        }
        cursor.close();
        return ingredList;
    }

}
