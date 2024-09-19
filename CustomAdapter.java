package com.example.appdevynmustard;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<InventoryItem> {

    private ArrayList<InventoryItem> itemList;
    private Context context;

    public CustomAdapter(Context context, ArrayList<InventoryItem> itemList) {
        super(context, 0, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_main, parent, false);
        }

        // Get the current item
        InventoryItem currentItem = itemList.get(position);

        // Converting view for each variable
        TextView nameTextView = convertView.findViewById(R.id.itemNameTextView);
        TextView priceTextView = convertView.findViewById(R.id.itemPriceTextView);
        TextView countTextView = convertView.findViewById(R.id.itemCountTextView);

        // Change text colors for low stock, no permissions for sms
        if (currentItem.getCount() <= 0) {
            countTextView.setTextColor(Color.parseColor("#ae000d"));
        } else if (currentItem.getCount() < 5) {
            countTextView.setTextColor(Color.parseColor("#cda400"));
        }

        // Alert user via SMS for low stock with permissions
        if (currentItem.getCount() == 0 && PreferenceAdapter.getBoolean(this.getContext(), "permission_value", false)) {
            Toast.makeText(this.getContext(), currentItem.getName() + " is out of stock!", Toast.LENGTH_SHORT).show();
        } else if (currentItem.getCount() < 5 && PreferenceAdapter.getBoolean(this.getContext(), "permission_value", false)) {
            Toast.makeText(this.getContext(), currentItem.getName() + " is nearly out of stock!", Toast.LENGTH_SHORT).show();
        }

        nameTextView.setText(currentItem.getName());
        priceTextView.setText(String.format("Price: $" + "%.2f", currentItem.getPrice()));
        countTextView.setText(String.valueOf("Quantity: " + currentItem.getCount()));

        return convertView;
    }
}