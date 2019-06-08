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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class addFood extends AppCompatActivity implements View.OnKeyListener{

    MyDB db;
    EditText editSearch; //editSearch is the plaintext where user enters food name
    EditText editAmount;
    Button btnAdd; // adds food from plaintext into db
    Button btnScan; // adds products by scanned barcode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        init();

        // adds user input to db
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editSearch.getText().toString();
                String t = editAmount.getText().toString();
                insertToDB(s, t);
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

        editAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //This function listens to when user hits the "check mark" button
            //and inputs plaintext to listview
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    String s = editSearch.getText().toString();
                    String t = editAmount.getText().toString();
                    insertToDB(s, t);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Helper function to input a non-empty plaintext string to listview in alphabetical order
     */
    public void insertToDB (String foodName, String amountFood){
        //Helper function to input a non empty plaintext string to db in alphabetical order
        if (foodName.equals("") && amountFood.equals("")){
            Toast.makeText(this, "Please enter a food and amount.", Toast.LENGTH_LONG).show();
            return;
        }
        db.addFood(foodName, amountFood);
        Toast.makeText(this, "You have stored " + amountFood + " " + foodName, Toast.LENGTH_LONG).show();
        editSearch.getText().clear();
        editAmount.getText().clear();
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    private void init() {
        db = MyDB.getInstance(this);
        editSearch = findViewById(R.id.editSearch);
        editAmount = findViewById(R.id.editAmount);
        btnAdd = findViewById(R.id.btnAdd);
        btnScan = findViewById(R.id.btnScan);
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
