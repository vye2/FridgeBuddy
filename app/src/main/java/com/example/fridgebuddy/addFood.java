package com.example.fridgebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class addFood extends AppCompatActivity implements View.OnKeyListener{


    MyDB db; //Haven't implemented db yet
    EditText searchFoods; //SearchFood is the plaintext where user enters food name
    public static ListView listView; //Scrollable list to show all food items
    Button btnAdd; //Adds food from plaintext into listView
    Button btnScan;
    ArrayAdapter<String> arrayAdapter;

    public ArrayList<String> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        searchFoods = findViewById(R.id.SearchFoods);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnScan = findViewById(R.id.btnScan);
        db = new MyDB(this, MyDB.DB_NAME, null, 1);

        // adds user input to listView
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = searchFoods.getText().toString();
                inputListView(s);
            }
        });

        // listener for scanning button
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scanCode.class));
            }
        });

        searchFoods.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //This function listens to when user hits the "check mark" button
            //and inputs plaintext to listview
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    String s = searchFoods.getText().toString();
                    inputListView(s);
                    return true;
                }
                return false;
            }
        });
    }

    //Helper function to input a non empty plaintext string to listview in alphabetical order
    public void inputListView (String input){
        if (input.equals("")){
            return;
        }
        foodList.add(input);
        searchFoods.getText().clear();
        Collections.sort(foodList);
        arrayAdapter = new ArrayAdapter<>(addFood.this, android.R.layout.simple_list_item_1, foodList);
        listView.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}
