package com.example.fridgebuddy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private ArrayList<FoodCard> foodCardList;

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView food_name;
        public TextView food_amount;
        public TextView purchase_date;

        public FoodViewHolder(View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_amount = itemView.findViewById(R.id.food_amount);
            purchase_date = itemView.findViewById(R.id.purchase_date);
        }
    }

    public FoodAdapter(ArrayList<FoodCard> foodList) {
        foodCardList = foodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_card_view, parent, false);
        FoodViewHolder fvh = new FoodViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        FoodCard currentItem = foodCardList.get(position);

        holder.food_name.setText(currentItem.getFood_name());
        holder.food_amount.setText(currentItem.getFood_amount());
        holder.purchase_date.setText(currentItem.getPurchase_date());
    }

    @Override
    public int getItemCount() {
        return foodCardList.size();
    }
}