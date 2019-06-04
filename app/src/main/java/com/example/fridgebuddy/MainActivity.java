package com.example.fridgebuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{

    MyDB db;

    private ArrayList<String> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodListAdapter adapter; // bridge between data and RecyclerView
    private RecyclerView.LayoutManager layoutManager;

    Button btnDelete;

    boolean[] checkedItems;
    String[] listItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Select Foods to Delete.");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked){
                            if (!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }
                            else {
                                if (mUserItems.contains(position)){
                                    mUserItems.remove(mUserItems.indexOf(position));
                                }
                            }
                        }
                    }
                });//might need string array

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserItems.clear();
                        for (int i = 0; i < checkedItems.length; i++){
                            if (checkedItems[i] == true){
                                mUserItems.add(i);
                            }
                        }
                        if (mUserItems.size() == 0){
                            Toast.makeText(MainActivity.this, "Please select at least one food item.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            for (int i = 0; i < mUserItems.size(); i++) {
                                db.removeFood(listItems[mUserItems.get(i)]);
                            }
                        }

                        listItems = db.getIngredListArr().toArray(new String[0]);
                        buildRecyclerView();

                    }
                });

                mBuilder.setNeutralButton("Select all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserItems.clear();
                        for (int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = true;
                            mUserItems.add(i);
                        }
                        btnDelete.performClick();
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                        }
                        mUserItems.clear();
                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });

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
        checkedItems = new boolean[db.getIngredListArr().size()];
        listItems = db.getIngredListArr().toArray(new String[0]);
        buildRecyclerView();

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
        btnDelete = findViewById(R.id.btn_delete);

        checkedItems = new boolean[db.getIngredListArr().size()];
        listItems = db.getIngredListArr().toArray(new String[0]);
    }

    public void buildRecyclerView() {
        // initialize foodList with all foods in db before passing to adapter
        foodList = db.getAllFoods();
        Collections.sort(foodList);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        adapter = new FoodListAdapter(MainActivity.this, foodList);

        SearchView searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
