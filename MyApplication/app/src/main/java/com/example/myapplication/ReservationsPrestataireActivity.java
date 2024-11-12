package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Reservation;
import com.example.myapplication.entity.ServicePres;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityReservationsPrestataireBinding;

import java.util.ArrayList;
import java.util.List;

public class ReservationsPrestataireActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReservations;
    private ReservationAdapter reservationAdapter;
    private AppDatabase database;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations_prestataire);

        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getAppDatabase(getApplicationContext());
        userId = getIntent().getIntExtra("userId", -1);

        loadReservations();
    }

    private void loadReservations() {
        List<ServicePres> userServices = database.servicePDao().getServicesByUserId(userId);
        List<Integer> serviceIds = new ArrayList<>();

        for (ServicePres service : userServices) {
            serviceIds.add(service.getId());
        }

        List<Reservation> reservations = database.reservationDao().getReservationsByServiceIds(serviceIds);
        reservationAdapter = new ReservationAdapter(this, reservations);
        recyclerViewReservations.setAdapter(reservationAdapter);
    }
}
