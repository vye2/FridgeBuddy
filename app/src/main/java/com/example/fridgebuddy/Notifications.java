package com.example.fridgebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Notifications extends AppCompatActivity {

    ListView expireList;
    ArrayList<String> listOfOld = new ArrayList<String>();
    ArrayList<String> topList = new ArrayList<String>();

    
    ArrayAdapter<String> adapter;
    MyDB db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        init();

        for (int i = 0; i < topList.size(); i++){
            String item = topList.get(i);
            List<String> splitList = Arrays.asList(item.split(","));
            String foodName = splitList.get(0);
            String dateName = splitList.get(1);
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String date1 = dateFormat.format(date);
                Date date2 = new SimpleDateFormat("yyyy/MM/dd").parse(date1);
                Date date3 = new SimpleDateFormat("yyyy/MM/dd").parse(dateName);

                DecimalFormat crunchifyFormatter = new DecimalFormat("###");
                long diff = date2.getTime() - date3.getTime();
                long diffDays = (int) (diff / (24*60*60*1000));
                listOfOld.add(foodName + " has been stored for " + diffDays + " days.");

                //String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            } catch (Exception e){
                e.printStackTrace();
            }
        }


        //listOfExpired.add("Whole Milk has been stored for 4 years 1 days");
        //listOfExpired.add("Vanilla Ice Cream has been stored for 6 months 11 days");
        //listOfExpired.add("Green Onion has been stored for 3 months 3 days");


    }

    private void init(){
        db = MyDB.getInstance(this);

        expireList = findViewById(R.id.expireList);
        topList = db.getTopXFoods("5");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfOld);

        expireList.setAdapter(adapter);

    }
}
