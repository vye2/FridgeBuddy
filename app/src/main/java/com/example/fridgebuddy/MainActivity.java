package com.example.fridgebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity{

    MyDB db;
    Button addFood;
    Button recipeMe;
    ImageButton NotifButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addFood.class);
                startActivity(intent);
            }
        });

        recipeMe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, recipeMe.class);
                startActivity(intent);
            }
        });

        NotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notifications.class);
                startActivity(intent);
            }
        });


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
        addFood = findViewById(R.id.StorageBtn);
        recipeMe = findViewById(R.id.RecipeMeButton);
        NotifButton = findViewById(R.id.NotifButton);
    }

}
