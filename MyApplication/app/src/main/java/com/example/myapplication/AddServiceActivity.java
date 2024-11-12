package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServiceCategory;
import com.example.myapplication.entity.ServicePres;

public class AddServiceActivity extends AppCompatActivity {

    private EditText editTextServiceName, editTextServiceDescription, editTextServicePrice;
    private Spinner spinnerCategory;
    private Button buttonAddService;
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServiceDescription = findViewById(R.id.editTextServiceDescription);
        editTextServicePrice = findViewById(R.id.editTextServicePrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddService = findViewById(R.id.buttonAddService);

        database = AppDatabase.getAppDatabase(getApplicationContext());
        sessionManager = new SessionManager(this);

        ArrayAdapter<ServiceCategory> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ServiceCategory.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });
    }

    private void addService() {
        String nom = editTextServiceName.getText().toString().trim();
        String description = editTextServiceDescription.getText().toString().trim();
        String prixText = editTextServicePrice.getText().toString().trim();
        ServiceCategory categorie = (ServiceCategory) spinnerCategory.getSelectedItem();

        if (nom.isEmpty() || description.isEmpty() || prixText.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un prix valide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get userId from session
        int userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "Erreur d'identification utilisateur", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and insert service
        ServicePres service = new ServicePres(nom, description, categorie, prix, userId);
        database.servicePDao().insertService(service);

        Toast.makeText(this, "Service ajouté avec succès!", Toast.LENGTH_SHORT).show();

        // Indicate the activity finished successfully to refresh the service list
        setResult(RESULT_OK);
        finish();
    }
}
