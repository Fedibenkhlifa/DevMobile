package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServicePres;

public class EditServiceActivity extends AppCompatActivity {

    private EditText editTextServiceName, editTextServiceDescription, editTextServicePrice;
    private Button buttonSaveChanges,buttonBack;
    private AppDatabase database;
    private ServicePres service;
    private int ratedUserId; // ID of the user being rated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServiceDescription = findViewById(R.id.editTextServiceDescription);
        editTextServicePrice = findViewById(R.id.editTextServicePrice);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonBack = findViewById(R.id.buttonBack); // Initialize the back button

        database = AppDatabase.getAppDatabase(this);

        int serviceId = getIntent().getIntExtra("serviceId", -1);
        service = database.servicePDao().getServiceById(serviceId);

        if (service != null) {
            editTextServiceName.setText(service.getNom());
            editTextServiceDescription.setText(service.getDescription());
            editTextServicePrice.setText(String.valueOf(service.getPrix()));
        }

        buttonSaveChanges.setOnClickListener(v -> {
            String name = editTextServiceName.getText().toString();
            String description = editTextServiceDescription.getText().toString();
            double price = Double.parseDouble(editTextServicePrice.getText().toString());

            service.setNom(name);
            service.setDescription(description);
            service.setPrix(price);

            database.servicePDao().updateService(service);
            Toast.makeText(this, "Service modifié avec succès", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK); // Indicate success
            finish(); // Close activity and return
        });
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(EditServiceActivity.this, UserInterfaceActivity.class);
            intent.putExtra("userId", ratedUserId); // Pass the rated user's ID back
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
    }
