package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServiceCategory;
import com.example.myapplication.entity.ServicePres;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AddServiceActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 2;

    private ImageView imageViewService;
    private Uri imageUri;
    private Button buttonSelectImageService, buttonAddService,buttonBack;
    private EditText editTextServiceName, editTextServiceDescription, editTextServicePrice;
    private Spinner spinnerCategory;
    private AppDatabase database;
    private SessionManager sessionManager;
    private int ratedUserId; // ID of the user being rated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        // Initialisation des vues
        imageViewService = findViewById(R.id.imageViewService);
        buttonSelectImageService = findViewById(R.id.buttonSelectImageService);
        buttonAddService = findViewById(R.id.buttonAddService);
        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServiceDescription = findViewById(R.id.editTextServiceDescription);
        editTextServicePrice = findViewById(R.id.editTextServicePrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonBack = findViewById(R.id.buttonBack); // Initialize the back button

        // Initialiser la base de données et le gestionnaire de session
        database = AppDatabase.getAppDatabase(getApplicationContext());
        sessionManager = new SessionManager(this);

        // Configuration de la sélection de l'image
        buttonSelectImageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Configurer le spinner pour les catégories de service
        ArrayAdapter<ServiceCategory> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ServiceCategory.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Configurer le bouton d'ajout de service
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddServiceActivity.this, UserInterfaceActivity.class);
            intent.putExtra("userId", ratedUserId); // Pass the rated user's ID back
            startActivity(intent);
            finish(); // Close the current activity
        });
    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewService.setImageURI(imageUri);
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File directory = new File(getFilesDir(), "service_images");
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(directory, System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

        // Récupérer l'ID de l'utilisateur à partir de la session
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Erreur d'identification utilisateur", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enregistrer l'image
        String imagePath = imageUri != null ? saveImageToInternalStorage(imageUri) : null;

        // Créer un service avec l'image
        ServicePres service = new ServicePres(nom, description, categorie, prix, userId, imagePath);
        database.servicePDao().insertService(service);

        Toast.makeText(this, "Service ajouté avec succès!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
