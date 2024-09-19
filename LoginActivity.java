package com.example.appdevynmustard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText userPassword;
    private EditText itemCounts;
    private user newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        newUser = new user(LoginActivity.this);

        // Button listeners
        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(this);

        userName = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
    }

    // Change Intent or add item based on button id.
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            if (newUser.verifyLogin(userName.getText().toString(), userPassword.getText().toString())) {
                Intent intent = new Intent(LoginActivity.this, add_item.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.register) {
            newUser.addUser(userName.getText().toString(), userPassword.getText().toString());
            Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
        }
    }
}