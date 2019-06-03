package com.example.fridgebuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class addFood extends AppCompatActivity implements View.OnKeyListener{

    MyDB db;
    EditText editSearch; //editSearch is the plaintext where user enters food name
    EditText editAmount;
    ListView listView;
    Button btnAdd; //Adds food from plaintext into listView
    Button btnScan; // adds products by scanned barcode
    //Button btnSelect; //Generates recipe based on
    //ArrayAdapter<String> arrayAdapter;
    FoodListAdapter adapter;

    //public ArrayList<String> foodList = new ArrayList<>();
    public ArrayList <String> foodListt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        init();

        // adds user input to listView
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editSearch.getText().toString();
                String t = editAmount.getText().toString();
                inputListView(s, t);
            }
        });

        // enters new activity to scan a food item's barcode with the phone camera
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(addFood.this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
                startActivity(new Intent(getApplicationContext(), ScanCode.class)); // try manually placing addFood.this

            }
        });

        /*btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
        editAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //This function listens to when user hits the "check mark" button
            //and inputs plaintext to listview
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    String s = editSearch.getText().toString();
                    String t = editAmount.getText().toString();
                    inputListView(s, t);
                    return true;
                }
                return false;
            }
        });

    }

    //Helper function to input a non empty plaintext string to listview in alphabetical order
    public void inputListView (String foodName, String amountFood){
        //Helper function to input a non empty plaintext string to listview in alphabetical order
        if (foodName.equals("") && amountFood.equals("")){
            Toast.makeText(this, "Please enter a food and amount.", Toast.LENGTH_LONG).show();
            return;
        }
        db.addFood(foodName, amountFood);
        editSearch.getText().clear();
        editAmount.getText().clear();
        foodListt = db.getAllFoods();
        Collections.sort(foodListt);
//        adapter = new FoodListAdapter(addFood.this, R.layout.adapter_view_layout, foodListt);
//        listView.setAdapter(adapter);

        //foodList = db.getAllFoods();
        //Collections.sort(foodList);
        //arrayAdapter = new ArrayAdapter<>(addFood.this, android.R.layout.simple_list_item_1, foodList);
        //listView.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    private void init() {
        db = MyDB.getInstance(this);
        editSearch = findViewById(R.id.editSearch);
        editAmount = findViewById(R.id.editAmount);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnScan = findViewById(R.id.btnScan);

        foodListt = db.getAllFoods();
        Collections.sort(foodListt);
//        adapter = new FoodListAdapter(addFood.this, R.layout.adapter_view_layout, foodListt);
//        listView.setAdapter(adapter);
        //foodList = db.getAllFoods(); //On create, display list view of sorted foods.
        //Collections.sort(foodList);
        //arrayAdapter = new ArrayAdapter<>(addFood.this, android.R.layout.simple_list_item_1, foodList);
        //listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(addFood.this, "Permission granted to access camera", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(addFood.this, "Permission denied to access camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions FridgeBuddy might request
        }
    }
}
