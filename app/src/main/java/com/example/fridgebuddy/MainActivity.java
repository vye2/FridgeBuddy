package com.example.fridgebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    MyDB db;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter; // bridge between data and RecyclerView
    private RecyclerView.LayoutManager layoutManager;
    Button add_food;
    Button delete_food;
    Button recipeMe;
    ImageButton NotifButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

//        addFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, addFood.class);
//                startActivity(intent);
//            }
//        });
//
//        recipeMe.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(MainActivity.this, recipeMe.class);
//                startActivity(intent);
//            }
//        });

        NotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notifications.class);
                startActivity(intent);
            }
        });
    }

    public void addFood(View view) {
        startActivity(new Intent(MainActivity.this, addFood.class));
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.deleteDatabase(db.getDatabaseName());
    }

    private void init() {
        db = MyDB.getInstance(this);
//        addFood = findViewById(R.id.StorageBtn);
//        recipeMe = findViewById(R.id.RecipeMeButton);
//        cardView = findViewById(R.id.card_view);

        // just testing
        ArrayList<FoodCard> foodList = new ArrayList<>();
        foodList.add(new FoodCard("rice", "2 lbs.", "02/22/19"));
        foodList.add(new FoodCard("apples", "5", "06/03/19"));
        foodList.add(new FoodCard("bananas", "12 lbs.", "06/03/19"));

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new FoodAdapter(foodList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        NotifButton = findViewById(R.id.NotifButton);
    }
}
