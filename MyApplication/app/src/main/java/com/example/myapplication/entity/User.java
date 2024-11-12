package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String prenom;
    private String adresse;
    private String numeroTel;
    private Role role;
    private String imageUri; // URI de l'image
    private String username; // Nouveau champ pour le nom d'utilisateur
    private String password; // Nouveau champ pour le mot de passe

    // Constructeur par défaut requis par Room
    public User() {
    }

    // Constructeur avec tous les paramètres
    @Ignore // Ignore par Room, utilisé pour créer des objets User manuellement
    public User(String nom, String prenom, String adresse, String numeroTel, Role role, String imageUri, String username, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numeroTel = numeroTel;
        this.role = role;
        this.imageUri = imageUri;
        this.username = username;
        this.password = password;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getNumeroTel() { return numeroTel; }
    public void setNumeroTel(String numeroTel) { this.numeroTel = numeroTel; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
