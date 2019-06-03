package com.example.fridgebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{

    MyDB db;

    private ArrayList<String> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter; // bridge between data and RecyclerView
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton notifButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void viewNotifications(View view) {
        startActivity(new Intent(MainActivity.this, Notifications.class));
    }

    public void addFood(View view) {
        startActivity(new Intent(MainActivity.this, addFood.class));
    }

    public void recipeMe(View view) {
        startActivity(new Intent(MainActivity.this, recipeMe.class));
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
    protected void onResume() {
        // refresh the adapter
        super.onResume();
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
//        this.deleteDatabase(db.getDatabaseName());
    }

    private void init() {
        db = MyDB.getInstance(this);

        buildRecyclerView();

        notifButton = findViewById(R.id.NotifButton);
    }

    public void buildRecyclerView() {
        // initialize foodList with all foods in db before passing to adapter
        foodList = db.getAllFoods();
        Collections.sort(foodList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        adapter = new FoodListAdapter(MainActivity.this, foodList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
