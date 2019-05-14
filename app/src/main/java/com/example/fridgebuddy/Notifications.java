package com.example.fridgebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {

    ListView expireList;
    ArrayList<String> listOfExpired = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        listOfExpired.add("Whole Milk has been stored for 4 years 1 days");
        listOfExpired.add("Vanilla Ice Cream has been stored for 6 months 11 days");
        listOfExpired.add("Green Onion has been stored for 3 months 3 days");

        expireList = findViewById(R.id.expireList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfExpired);
        expireList.setAdapter(adapter);
    }
}
