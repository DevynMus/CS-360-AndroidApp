package com.example.appdevynmustard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;

public class add_item extends AppCompatActivity implements View.OnClickListener {
    private EditText itemNames;
    private EditText itemPrices;
    private EditText itemCounts;
    private inventory systemInventory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_item);

        Switch permSwitch = findViewById(R.id.smsSwitch);

        Button createItem = (Button) findViewById(R.id.createButton);
        createItem.setOnClickListener(this);


        Button moveToTable = (Button) findViewById(R.id.changeToTableView);
        moveToTable.setOnClickListener(this);

        // Declare button variables and click listener
        systemInventory = new inventory(add_item.this);
        moveToTable = findViewById(R.id.changeToTableView);

        // Declare listener for button and switch click
        String booleanVar = "permission_value";
        moveToTable.setOnClickListener(this);
        permSwitch.setChecked(PreferenceAdapter.getBoolean(this, booleanVar, false));
        // Switch function that saves permissions to local storage
        permSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean notificationBool = permSwitch.isChecked();
            PreferenceAdapter.saveBoolean(this, booleanVar, notificationBool);
            // display message if notifications are enabled or disabled
            if (PreferenceAdapter.getBoolean(this, booleanVar, false) == true) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }

        });
        // Declare item text input variables by ID
        itemNames = findViewById(R.id.itemName);
        itemPrices = findViewById(R.id.itemPrice);
        itemCounts = findViewById(R.id.itemCount);
    }

    // Change Intent or add item based on button id.
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createButton) {
            float itemPriceFLT = Float.valueOf(itemPrices.getText().toString());
            int itemCountINT = Integer.parseInt(itemCounts.getText().toString());
            systemInventory.addItem(itemNames.getText().toString(), itemPriceFLT, itemCountINT);
        } else if (v.getId() == R.id.changeToTableView) {
            Intent intent = new Intent(add_item.this, MainActivity.class);
            startActivity(intent);
        }
    }
}