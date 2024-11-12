package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignup; // Ajoutez buttonSignup
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup); // Initialisez buttonSignup

        database = AppDatabase.getAppDatabase(this);
        sessionManager = new SessionManager(this);

        // Action pour le bouton Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                User user = authenticateUser(username, password);
                if (user != null) {
                    sessionManager.saveUserSession(user.getId(), user.getRole().toString());
                    navigateToNextActivity(user.getRole().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Action pour le bouton Signup
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers AddUserActivity
                Intent intent = new Intent(LoginActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private User authenticateUser(String username, String password) {
        User user = database.userDao().findUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    private void navigateToNextActivity(String role) {
        Intent intent;
        if ("CLIENT".equals(role)) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        } else if ("PRESTATAIRE".equals(role)) {
            intent = new Intent(LoginActivity.this, UserInterfaceActivity.class);
        } else {
            Toast.makeText(this, "Role inconnu", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        finish();
    }
}
