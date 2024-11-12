package com.example.myapplication;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getAppDatabase(getApplicationContext());
        userId = getIntent().getIntExtra("userId", -1);

        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservationList = database.reservationDao().getReservationsByUserId(userId);
        reservationAdapter = new ReservationAdapterFront(this, reservationList);
        recyclerViewReservations.setAdapter(reservationAdapter);
    }
}
