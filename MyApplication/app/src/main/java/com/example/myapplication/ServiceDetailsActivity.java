package com.example.myapplication;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Reservation;
import com.example.myapplication.SessionManager;
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ServiceDetailsActivity extends AppCompatActivity {

    private TextView textViewReservationDate;
    private Button buttonReserveService;
    private Calendar reservationDate;
    private AppDatabase database;
    private SessionManager sessionManager;
    private int serviceId;
    private ImageView imageViewService, imageViewUser; // Ajoutez imageViewUser pour l'image de l'utilisateur
    private TextView textViewUserName; // Ajoutez textViewUserName pour le nom de l'utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        imageViewService = findViewById(R.id.imageViewService); // Image du service
        imageViewUser = findViewById(R.id.imageViewUser); // Image de l'utilisateur
        textViewUserName = findViewById(R.id.textViewUserName); // Nom de l'utilisateur
        textViewReservationDate = findViewById(R.id.textViewReservationDate);
        buttonReserveService = findViewById(R.id.buttonReserveService);

        TextView textViewServiceName = findViewById(R.id.textViewServiceName);
        TextView textViewServiceDescription = findViewById(R.id.textViewServiceDescription);
        TextView textViewServiceCategory = findViewById(R.id.textViewServiceCategory);
        TextView textViewServicePrice = findViewById(R.id.textViewServicePrice);

        reservationDate = Calendar.getInstance();
        database = AppDatabase.getAppDatabase(getApplicationContext());
        sessionManager = new SessionManager(this);

        serviceId = getIntent().getIntExtra("serviceId", -1);

        ServicePres service = database.servicePDao().getServiceById(serviceId);
        if (service != null) {
            // Affichez les informations du service
            textViewServiceName.setText(service.getNom());
            textViewServiceDescription.setText(service.getDescription());
            textViewServiceCategory.setText(service.getCategorie().toString());
            textViewServicePrice.setText(String.format("$%.2f", service.getPrix()));
            if (service.getImagePath() != null) {
                File imageFile = new File(service.getImagePath());
                if (imageFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(service.getImagePath());
                    imageViewService.setImageBitmap(bitmap);
                }
            }

            // Récupérez les informations de l'utilisateur qui a créé le service
            User user = database.userDao().getUserById(service.getUserId());
            if (user != null) {
                textViewUserName.setText(user.getNom() + " " + user.getPrenom());
                if (user.getImageUri() != null) {
                    File userImageFile = new File(user.getImageUri());
                    if (userImageFile.exists()) {
                        Bitmap userBitmap = BitmapFactory.decodeFile(user.getImageUri());
                        imageViewUser.setImageBitmap(userBitmap);
                    }
                }
            } else {
                textViewUserName.setText("Utilisateur inconnu");
            }

        } else {
            Toast.makeText(this, "Service details not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        textViewReservationDate.setOnClickListener(v -> showDatePicker());
        buttonReserveService.setOnClickListener(v -> reserveService(service));
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

        // Définir la date minimale (date actuelle)
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }


    private void reserveService(ServicePres service) {
        if (textViewReservationDate.getText().equals("Select Reservation Date")) {
            Toast.makeText(this, "Please select a reservation date.", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();

        if (userId == service.getUserId()) {
            Toast.makeText(this, "Vous ne pouvez pas réserver votre propre service.", Toast.LENGTH_SHORT).show();
            return;
        }

        Date selectedDate = reservationDate.getTime();
        Reservation reservation = new Reservation(serviceId, userId, selectedDate);
        database.reservationDao().insertReservation(reservation);

        Toast.makeText(this, "Service réservé avec succès!", Toast.LENGTH_LONG).show();
    }
}
