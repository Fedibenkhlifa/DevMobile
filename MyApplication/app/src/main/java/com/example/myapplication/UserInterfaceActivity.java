package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServicePres;
import java.util.List;

public class UserInterfaceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewServices;
    private ServiceAdapterPr serviceAdapter;
    private AppDatabase database;
    private SessionManager sessionManager;
    private Button buttonViewAllServices, buttonAddService, buttonLogout, buttonMyReservations;
    private static final int REQUEST_CODE_ADD_SERVICE = 1;
    public static final int REQUEST_CODE_EDIT_SERVICE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));

        buttonViewAllServices = findViewById(R.id.buttonViewAllServices);
        buttonAddService = findViewById(R.id.buttonAddService);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonMyReservations = findViewById(R.id.buttonMyReservations);  // New button for "Mes Réservations"

        sessionManager = new SessionManager(this);
        database = AppDatabase.getAppDatabase(getApplicationContext());

        int userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "Erreur d'identification utilisateur", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserServices(userId);

        buttonViewAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInterfaceActivity.this, DisplayServicesActivity.class);
                intent.putExtra("userId", -1);
                startActivity(intent);
            }
        });

        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInterfaceActivity.this, AddServiceActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_SERVICE);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Toast.makeText(UserInterfaceActivity.this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserInterfaceActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        buttonMyReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInterfaceActivity.this, ReservationsPrestataireActivity.class);
                intent.putExtra("userId", userId);  // Pass the logged-in user's ID to the new activity
                startActivity(intent);
            }
        });
    }

    private void loadUserServices(int userId) {
        List<ServicePres> serviceList = database.servicePDao().getServicesByUserId(userId);
        if (serviceAdapter == null) {
            serviceAdapter = new ServiceAdapterPr(this, serviceList, database);
            recyclerViewServices.setAdapter(serviceAdapter);
        } else {
            serviceAdapter.updateServiceList(serviceList); // Met à jour la liste des services
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ADD_SERVICE || requestCode == REQUEST_CODE_EDIT_SERVICE) && resultCode == RESULT_OK) {
            int userId = sessionManager.getUserId();
            loadUserServices(userId); // Recharge les services
        }
    }


}
