package com.example.fridgebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Implements a scanner to scan UPC codes (barcodes). Processes UPC codes and adds food names and
 * amount to the db.
 */
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

    /**
     * Calls this function with the UPC code and adds the resulting food name and amount to the db.
     * @param UPCode the code to process
     */
    public void getFood(String UPCode){
        String url = "https://nutritionix-api.p.rapidapi.com/v1_1/item?upc=" + UPCode;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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

        // don't return to MainActivity immediately
        // allow user to scan multiple items quickly
    }

    private void init() {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

        db = MyDB.getInstance(this);
    }
}
