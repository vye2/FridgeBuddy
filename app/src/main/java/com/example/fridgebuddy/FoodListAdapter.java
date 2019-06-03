package com.example.fridgebuddy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private ArrayList<String> foodList;
    private Context mContext;

    public FoodListAdapter(Context context, ArrayList<String> foodList) {
        mContext = context;
        this.foodList = foodList;
    }

    @Override
    public FoodListAdapter.FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card_view,parent,false);
        FoodViewHolder viewHolder = new FoodViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.FoodViewHolder holder, int position) {
        String item = foodList.get(position);
        List<String> splitList = Arrays.asList(item.split(","));
        String foodName = splitList.get(0);
        String foodAmount = splitList.get(1);
        String purchaseDate = splitList.get(2);

        holder.food_name.setText(foodName);
        holder.food_amount.setText(foodAmount);
        holder.purchase_date.setText(purchaseDate);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        protected TextView food_name;
        protected TextView food_amount;
        protected TextView purchase_date;

        public FoodViewHolder(View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_amount = itemView.findViewById(R.id.food_amount);
            purchase_date = itemView.findViewById(R.id.purchase_date);
        }
    }
}
