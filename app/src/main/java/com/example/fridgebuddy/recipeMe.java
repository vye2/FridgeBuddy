package com.example.fridgebuddy;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class recipeMe extends AppCompatActivity {

    MyDB db;
    TextView RecipeTitle;
    TextView Instructions;
    ListView IngredList;
    Button btnRandomRecipe;
    Button btnRecipeMe;
    boolean[] checkedItems;
    String[] listItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    ArrayAdapter<String> arrayAdapter;

    private RequestQueue requestQueue;

    static public ArrayList<String> ingredientsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_me);

        init();

        requestQueue = Volley.newRequestQueue(this);

        btnRandomRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randRecipe();
            }
        });

        btnRecipeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(recipeMe.this);
                mBuilder.setTitle("RecipeMe with Selected Foods.");
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
                mBuilder.setPositiveButton("Generate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mUserItems.size() == 0){
                            Toast.makeText(recipeMe.this, "Please select at least one food item.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String item = "";
                            for (int i = 0; i < mUserItems.size(); i++) {
                                item = item + listItems[mUserItems.get(i)];
                                if (i != mUserItems.size() - 1) {
                                    item = item + ",";
                                }
                            }
                            System.out.println(item);
                            relatedRecipeID(item);
                        }
                    }
                });

//                mBuilder.setNeutralButton("Select all", null);

                mBuilder.setNeutralButton("Select all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = true;
                            mUserItems.add(i);
                        }
                        btnRecipeMe.performClick();
                        /*
                        mUserItems.clear();
                        String item = "";
                        for (int i = 0; i < checkedItems.length; i++){
                            item = item + listItems[i];
                            if (i!= listItems.length - 1){
                                item = item + ",";
                            }
                            mUserItems.add(i);
                        }
                        relatedRecipeID(item);
                        */
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });

    }

    public void relatedRecipeID(String selected){
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?number=5&ranking=1&ignorePantry=true&ingredients=";
        //url = url + db.getIngredList();
        url = url + selected;
        System.out.println(url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                //JsonObjectRequest request = new JSONArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Random rand = new Random();
                            int val = rand.nextInt(5);
                            JSONObject recipe = response.getJSONObject(val);
                            int returnedID = recipe.getInt("id");
                            getRecipe(returnedID);
                            System.out.println("Test: " + returnedID);
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

    public void getRecipe(int recipeID){
        System.out.println("After: " + recipeID);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + recipeID + "/information";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String title = response.getString("title");
                            RecipeTitle.setText(title);
                            String instr = response.getString("instructions");
                            Instructions.setText(instr);

                            JSONArray extendedIngredients = response.getJSONArray("extendedIngredients");
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

    private void init() {
        db = MyDB.getInstance(this);
        RecipeTitle = findViewById(R.id.RecipeTitle);
        Instructions = findViewById(R.id.Instructions);
        IngredList = findViewById(R.id.IngredList);
        btnRandomRecipe = findViewById(R.id.BtnRandom);
        btnRecipeMe = findViewById(R.id.BtnRecipeMe);
        checkedItems = new boolean[db.getIngredListArr().size()];
        listItems = db.getIngredListArr().toArray(new String[0]);
    }
}
