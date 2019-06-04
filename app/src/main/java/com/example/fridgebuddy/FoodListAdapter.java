package com.example.fridgebuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>
        implements Filterable{

    private ArrayList<String> foodList;
    private ArrayList<String> foodListFull;
    private Context mContext;

    public FoodListAdapter(Context context, ArrayList<String> foodList) {
        mContext = context;
        this.foodList = foodList;
        foodListFull = new ArrayList<>(foodList); // another list for search
    }

    @NonNull
    @Override
    public FoodListAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card_view,parent,false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
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
        TextView food_name;
        TextView food_amount;
        TextView purchase_date;

        public FoodViewHolder(View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_amount = itemView.findViewById(R.id.food_amount);
            purchase_date = itemView.findViewById(R.id.purchase_date);
        }
    }

    @Override
    public Filter getFilter() {
        return foodFilter;
    }

    // filter for search
    private Filter foodFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredList = new ArrayList<>();

            // nothing filtered
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(foodListFull);
            }
            // filtered
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String item : foodListFull) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        // displays only filtered items in the RecycleView
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foodList.clear();
            foodList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
