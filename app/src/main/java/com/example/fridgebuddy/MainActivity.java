package com.example.fridgebuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    SharedPreferences sh;
    Button addFood;
    Button recipeMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sh = getSharedPreferences("myPrefs", MODE_PRIVATE);

        addFood = findViewById(R.id.StorageBtn);
        recipeMe = findViewById(R.id.RecipeMeButton);
        
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
    }


}
