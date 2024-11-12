package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.example.myapplication.entity.Role;
import com.example.myapplication.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AddUserActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Code de requête pour l'intention de sélection d'image

    private EditText editTextNom, editTextPrenom, editTextAdresse, editTextNumeroTel, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private ImageView imageViewProfile;
    private Button buttonSelectImage, buttonSaveUser;
    private Uri imageUri;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_activity); // Assurez-vous que le nom du layout est correct

        // Initialisation des vues et de la base de données
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        editTextNumeroTel = findViewById(R.id.editTextNumeroTel);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSaveUser = findViewById(R.id.buttonAddUser); // Correction du nom de l'ID

        // Initialiser la base de données
        database = AppDatabase.getAppDatabase(getApplicationContext());

        // Configuration de la sélection de l'image
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter); // Associe l'adaptateur au Spinner
        // Sauvegarder l'utilisateur
        buttonSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
    }

    // Ouvrir le sélecteur d'images pour choisir une image
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionnez une image"), PICK_IMAGE_REQUEST);
    }

    // Gérer le résultat de la sélection d'image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewProfile.setImageURI(imageUri); // Afficher l'image sélectionnée
        }
    }

    // Sauvegarder l'utilisateur dans la base de données
    private void saveUser() {
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String adresse = editTextAdresse.getText().toString().trim();
        String numeroTel = editTextNumeroTel.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String roleStr = spinnerRole.getSelectedItem().toString().trim();

        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || numeroTel.isEmpty() || roleStr.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Rôle invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enregistrer l'image dans le stockage interne et obtenir le chemin
        String imagePath = imageUri != null ? saveImageToInternalStorage(imageUri) : null;

        // Créer l'objet utilisateur avec le chemin local de l'image
        User user = new User(nom, prenom, adresse, numeroTel, role, imagePath, username, password);
        database.userDao().insertUser(user);

        Toast.makeText(this, "Utilisateur ajouté avec succès!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File directory = new File(getFilesDir(), "images");
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

}
