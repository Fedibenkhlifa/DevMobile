package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Reservation;
import java.util.List;

public class MyReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReservations;
    private ReservationAdapterFront reservationAdapter;
    private AppDatabase database;
    private int userId;
private Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
        buttonBack = findViewById(R.id.buttonBack); // Initialize the back button

        database = AppDatabase.getAppDatabase(getApplicationContext());
        userId = getIntent().getIntExtra("userId", -1);

        loadReservations();
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(MyReservationsActivity.this, MainActivity.class);
            intent.putExtra("userId", userId); // Pass the rated user's ID back
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void loadReservations() {
        List<Reservation> reservationList = database.reservationDao().getReservationsByUserId(userId);
        reservationAdapter = new ReservationAdapterFront(this, reservationList);
        recyclerViewReservations.setAdapter(reservationAdapter);
    }
}
