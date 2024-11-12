package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.User;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private Button buttonViewServices, buttonLogout, buttonMyReservations; // Add buttonMyReservations
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        buttonViewServices = findViewById(R.id.buttonViewServices);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonMyReservations = findViewById(R.id.buttonMyReservations); // Initialize the new button

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getAppDatabase(getApplicationContext());

        loadUsers();

        buttonViewServices.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisplayServicesActivity.class);
            startActivity(intent);
        });

        buttonMyReservations.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyReservationsActivity.class);
            intent.putExtra("userId", sessionManager.getUserId()); // Pass the current user ID
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            sessionManager.logout();
            navigateToLogin();
        });
    }

    private void loadUsers() {
        List<User> prestataireList = database.userDao().getUsersByRole("PRESTATAIRE");
        userAdapter = new UserAdapter(this, prestataireList);
        recyclerViewUsers.setAdapter(userAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadUsers();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
