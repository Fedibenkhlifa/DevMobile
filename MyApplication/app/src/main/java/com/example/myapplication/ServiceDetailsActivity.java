package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Reservation;
import com.example.myapplication.SessionManager;
import com.example.myapplication.entity.ServicePres;

import java.util.Calendar;
import java.util.Date;

public class ServiceDetailsActivity extends AppCompatActivity {

    private TextView textViewReservationDate;
    private Button buttonReserveService;
    private Calendar reservationDate;
    private AppDatabase database;
    private SessionManager sessionManager;
    private int serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        textViewReservationDate = findViewById(R.id.textViewReservationDate);
        buttonReserveService = findViewById(R.id.buttonReserveService);

        TextView textViewServiceName = findViewById(R.id.textViewServiceName);
        TextView textViewServiceDescription = findViewById(R.id.textViewServiceDescription);
        TextView textViewServiceCategory = findViewById(R.id.textViewServiceCategory);
        TextView textViewServicePrice = findViewById(R.id.textViewServicePrice);

        reservationDate = Calendar.getInstance();
        database = AppDatabase.getAppDatabase(getApplicationContext());
        sessionManager = new SessionManager(this);

        // Get the service ID passed to this activity
        serviceId = getIntent().getIntExtra("serviceId", -1);

        // Fetch service details based on serviceId
        ServicePres service = database.servicePDao().getServiceById(serviceId);
        if (service != null) {
            textViewServiceName.setText(service.getNom());
            textViewServiceDescription.setText(service.getDescription());
            textViewServiceCategory.setText(service.getCategorie().toString());
            textViewServicePrice.setText(String.format("$%.2f", service.getPrix()));
        } else {
            Toast.makeText(this, "Service details not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        textViewReservationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonReserveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveService();
            }
        });
    }

    private void showDatePicker() {
        int year = reservationDate.get(Calendar.YEAR);
        int month = reservationDate.get(Calendar.MONTH);
        int day = reservationDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    reservationDate.set(selectedYear, selectedMonth, selectedDay);
                    textViewReservationDate.setText(
                            selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void reserveService() {
        if (textViewReservationDate.getText().equals("Select Reservation Date")) {
            Toast.makeText(this, "Please select a reservation date.", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        Date selectedDate = reservationDate.getTime();

        Reservation reservation = new Reservation(serviceId, userId, selectedDate);
        database.reservationDao().insertReservation(reservation);

        Toast.makeText(this, "Service reserved successfully!", Toast.LENGTH_LONG).show();
    }
}
