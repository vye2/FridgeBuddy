package com.example.fridgebuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    int mResource;

    public FoodListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = getItem(position);
        List<String> splitList = Arrays.asList(item.split(","));
        String foodName = splitList.get(0);
        String amountName = splitList.get(1);
        String dateName = splitList.get(2);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView txtFoodName = (TextView) convertView.findViewById(R.id.txtFoodName);
        TextView txtAmount = (TextView) convertView.findViewById(R.id.textAmount);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);

        txtFoodName.setText(foodName);
        txtAmount.setText(amountName);
        txtDate.setText(dateName);

        return convertView;
    }
}
