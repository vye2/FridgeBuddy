package com.example.fridgebuddy;

public class FoodHelper {

    //private int foodID; //primary key of food
    private String foodName;
    //private String dateStored;
    private String amountStored;

    public FoodHelper(String foodName, String amountStored){
        setFoodName(foodName);
        setAmountStored(amountStored);

    }

    public String getFoodName(){
        return foodName;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public String getAmountStored(){
        return amountStored;
    }

    public void setAmountStored(String amountStored){
        this.amountStored = amountStored;
    }


}

