package com.example.fridgebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class recipeMe extends AppCompatActivity {


    static final String APIHost = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
    static final String APIKey = "6b8c4aa81bmsh697ceaf1cee4e05p1c84f7jsnf7ba2242e192";
    TextView RecipeTitle;
    TextView Instructions;
    ListView IngredList;
    ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> ingredientsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_me);

        RecipeTitle = findViewById(R.id.RecipeTitle);
        Instructions = findViewById(R.id.Instructions);
        IngredList = findViewById(R.id.IngredList);


        try {
            randomRecipe();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void randomRecipe() throws UnirestException{
        HttpResponse<JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=1&tags=vegetarian%2Cdessert")
                .header("X-RapidAPI-Host", APIHost)
                .header("X-RapidAPI-Key", APIKey)
                .asJson();

        try{

            JSONObject myObj = response.getBody().getObject();
            JSONArray array = myObj.getJSONArray("recipes");
            JSONObject firstRecipe = array.getJSONObject(0);
            String title = firstRecipe.getString("title");
            RecipeTitle.setText(title);
            Instructions.setText(firstRecipe.getString("instructions"));
            JSONArray extendedIngredients = firstRecipe.getJSONArray("extendedIngredients");
            for (int i = 0; i < extendedIngredients.length(); i++){
                JSONObject ingredI = extendedIngredients.getJSONObject(i);
                String originalString = ingredI.getString("originalString");
                if (!originalString.equals("")){
                    ingredientsList.add(originalString);
                }
            }
            arrayAdapter = new ArrayAdapter<>(recipeMe.this, android.R.layout.simple_list_item_1, ingredientsList);
            IngredList.setAdapter(arrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
