package com.example.fridgebuddy;

import android.content.Intent;
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


    EditText editSearch; //editSearch is the plaintext where user enters food name
    EditText editAmount;
    ListView listView;
    Button btnAdd; //Adds food from plaintext into listView
    Button btnScan;
    ArrayAdapter<String> arrayAdapter;

    public ArrayList<String> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        editSearch = findViewById(R.id.editSearch);
        editAmount = findViewById(R.id.editAmount);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnScan = findViewById(R.id.btnScan);

        foodList = MainActivity.db.getAllFoods(); //On create, display list view of sorted foods.
        Collections.sort(foodList);
        arrayAdapter = new ArrayAdapter<>(addFood.this, android.R.layout.simple_list_item_1, foodList);
        listView.setAdapter(arrayAdapter);

        // adds user input to listView
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editSearch.getText().toString();
                String t = editAmount.getText().toString();
                inputListView(s, t);
            }
        });

        // listener for scanning button
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scanCode.class));
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
        MainActivity.db.addFood(foodName, amountFood);
        editSearch.getText().clear();
        editAmount.getText().clear();
        foodList = MainActivity.db.getAllFoods();
        Collections.sort(foodList);
        arrayAdapter = new ArrayAdapter<>(addFood.this, android.R.layout.simple_list_item_1, foodList);
        listView.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}
