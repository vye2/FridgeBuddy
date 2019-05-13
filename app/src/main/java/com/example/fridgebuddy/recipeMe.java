package com.example.fridgebuddy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class recipeMe extends AppCompatActivity {

    TextView RecipeTitle;
    TextView Instructions;
    ListView IngredList;
    Button btnRandomRecipe;
    Button btnRecipeMe;

    ArrayAdapter<String> arrayAdapter;

    private RequestQueue requestQueue;

    static public ArrayList<String> ingredientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_me);
        RecipeTitle = findViewById(R.id.RecipeTitle);
        Instructions = findViewById(R.id.Instructions);
        IngredList = findViewById(R.id.IngredList);
        btnRandomRecipe = findViewById(R.id.BtnRandom);
        btnRecipeMe = findViewById(R.id.BtnRecipeMe);

        requestQueue = Volley.newRequestQueue(this);

        btnRandomRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randRecipe();
            }
        });
    }

    public void randRecipe(){
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("recipes");
                            JSONObject firstRecipe = array.getJSONObject(0);
                            String title = firstRecipe.getString("title");
                            RecipeTitle.setText(title);
                            String instr = firstRecipe.getString("instructions");
                            Instructions.setText(instr);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String > params = new HashMap<String, String>();
                params.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "fc6fbf058emsh24e3e2cb5fcda03p12ffd1jsn451c757b8d26");
                return params;
            }
        };
        requestQueue.add(request);
    }
}
