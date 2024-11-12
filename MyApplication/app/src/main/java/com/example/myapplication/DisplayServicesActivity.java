package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

import java.util.List;

public class DisplayServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private AppDatabase database;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_services);

        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));



        // Obtenir l'ID de l'utilisateur à partir de l'Intent
        userId = getIntent().getIntExtra("userId", -1);

        database = AppDatabase.getAppDatabase(getApplicationContext());

        loadServices();
    }

    private void loadServices() {
        List<ServicePres> serviceList;
        if (userId != -1) {
            // Charger les services spécifiques à l'utilisateur
            serviceList = database.servicePDao().getServicesByUserId(userId);
        } else {
            // Charger tous les services si aucun utilisateur spécifique n'est sélectionné
            serviceList = database.servicePDao().getAllServices();
        }

        List<User> userList = database.userDao().getAllUsers();
        serviceAdapter = new ServiceAdapter(this, serviceList, userList);
        recyclerViewServices.setAdapter(serviceAdapter);
    }
}
