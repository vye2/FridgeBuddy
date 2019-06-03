package com.example.fridgebuddy;

// this is FoodHelper but with a date. might just replace the FoodHelper.

public class FoodCard {
    private String food_name;
    private String food_amount;
    private String purchase_date;

    public FoodCard(String name, String amount, String date) {
        food_name = name;
        food_amount = amount;
        purchase_date = date;
    }

    public String getFood_name() {
        return food_name;
    }

    public String getFood_amount() {
        return food_amount;
    }

    public String getPurchase_date() {
        return purchase_date;
    }
}