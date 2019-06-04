package com.example.fridgebuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    MyDB db;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        init();
        requestQueue = Volley.newRequestQueue(this);
        //Log.d("ScanCode", "OnCreate");
        getFood("028400047913");

    }

    @Override
    protected void onPause () {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        getFood(result.getText());
        zXingScannerView.resumeCameraPreview(this);
    }

    public void getFood(String UPCode){    /////////////call this function with UPC code. Should add food name and food amount to db.
        String url = "https://nutritionix-api.p.rapidapi.com/v1_1/item?upc=" + UPCode;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println("TESTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                            String foodName = response.getString("item_name");
                            foodName = foodName.replace(",", " ");
                            foodName = foodName.replace("\'", "");
                            double numberServings = response.getDouble("nf_servings_per_container");
                            double servingSize = response.getDouble("nf_serving_size_qty");
                            String units = response.getString("nf_serving_size_unit");
                            double servings = numberServings * servingSize;
                            String foodAmount = (String.valueOf(servings)) + " " + units;
                            db.addFood(foodName, foodAmount);

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
                params.put("X-RapidAPI-Host", "nutritionix-api.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "fc6fbf058emsh24e3e2cb5fcda03p12ffd1jsn451c757b8d26");
                return params;
            }
        };
        requestQueue.add(request);

        // return to MainActivity
        //startActivity(new Intent(ScanCode.this, MainActivity.class));
    }

    private void init() {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

        db = MyDB.getInstance(this);
    }
}
