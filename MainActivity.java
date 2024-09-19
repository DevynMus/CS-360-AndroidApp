package com.example.appdevynmustard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button helloBtn;
    private TextView greetingTxt;
    private inventory dataInventory;
    private TextView dataTablePrint;
    private EditText nameEdit = null;
    private ListView listView;
    private Button addButton;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            dataInventory = new inventory(this);
        ListView listView = findViewById(R.id.listView);
        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.footer,null);
        addButton = (Button) footerLayout.findViewById(R.id.addItemBtn);

        listView.addFooterView(addButton);
        addButton.setOnClickListener(this);

        inventory dbHandler = new inventory(this);

        // Retrieve data from the database
        ArrayList<InventoryItem> itemList = new ArrayList<>();
        Cursor cursor = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM " + inventory.inventoryTable.TABLE, null);

        // Iterate through the table
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String itemName = cursor.getString((cursor.getColumnIndex(inventory.inventoryTable.COL_NAME)));
                @SuppressLint("Range") float itemPrice = cursor.getFloat((cursor.getColumnIndex(inventory.inventoryTable.COL_PRICE)));
                @SuppressLint("Range") int itemCount = cursor.getInt((cursor.getColumnIndex(inventory.inventoryTable.COL_COUNT)));

                // Create a new InventoryItem instance
                InventoryItem item = new InventoryItem(itemName, itemPrice, itemCount);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // Create customer adapter to link data to ListView
        CustomAdapter adapter = new CustomAdapter(this, itemList);
        listView.setAdapter(adapter);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected item from the itemList onClick()
            InventoryItem selectedItem = itemList.get(position);

            // Build app dialog for the user to select update or delete function per item
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Select Action")
                    .setMessage("Choose an action for item: " + selectedItem.getName())
                    .setPositiveButton("Update", (dialog, which) -> {
                        showEditDialog(selectedItem);
                    })
                    .setNegativeButton("Delete", (dialog, which) -> {
                        deleteItem(selectedItem);
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public void onClick(View v) {
        // Change intent on add button click
        if (v.getId() == R.id.addItemBtn) {
            Intent intent = new Intent(MainActivity.this, add_item.class);
            startActivity(intent);;
        }
    }

    private void showEditDialog(InventoryItem item) {
        int newId = dataInventory.GetId(item.getName());
        AlertDialog.Builder editDialogBuilder;
        editDialogBuilder = new AlertDialog.Builder(this);
        editDialogBuilder.setTitle("Edit Item");
        View editDialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
        editDialogBuilder.setView(editDialogView);

        // Declare text vars
        EditText editName = editDialogView.findViewById(R.id.editName);
        EditText editPrice = editDialogView.findViewById(R.id.editPrice);
        EditText editCount = editDialogView.findViewById(R.id.editCount);

        // Set text values
        editName.setText(item.getName());
        editPrice.setText(String.valueOf(item.getPrice()));
        editCount.setText(String.valueOf(item.getCount()));

        editDialogBuilder.setPositiveButton("Save", (dialog, which) -> {
            String updatedName = editName.getText().toString().trim();
            float updatedPrice = Float.parseFloat(editPrice.getText().toString().trim());
            int updatedCount = Integer.parseInt(editCount.getText().toString().trim());

            // Update Item info
            item.setName(updatedName);
            item.setPrice(updatedPrice);
            item.setCount(updatedCount);

            dataInventory.update_item(item, newId);
            recreate();
        });

        editDialogBuilder.setNegativeButton("Cancel", null);
        editDialogBuilder.show();
    }

    // Implement the deleteItem method to delete the selected item
    private void deleteItem(InventoryItem item) {
        // Alert user to be sure of deletion
        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this);
        deleteDialogBuilder.setTitle("Confirm Deletion");
        deleteDialogBuilder.setMessage("Are you sure you want to delete item: " + item.getName() + "?");
        deleteDialogBuilder.setPositiveButton("Delete", (dialog, which) -> {
            // Delete the item from the database
            int newId = dataInventory.GetId(item.getName());

            dataInventory.delete_item(item, newId);

            // Refresh the app screen
            recreate();
        });
        deleteDialogBuilder.setNegativeButton("Cancel", null);
        deleteDialogBuilder.show();
    }

}