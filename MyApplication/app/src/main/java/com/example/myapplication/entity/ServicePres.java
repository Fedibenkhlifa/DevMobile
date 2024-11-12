package com.example.myapplication.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "servicepr")

public class ServicePres {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String description;
    private ServiceCategory categorie;
    private double prix;
    private int userId; // Id du prestataire (clé étrangère vers User)
    public ServicePres() {
    }

    public ServicePres(String nom, String description, ServiceCategory categorie, double prix, int userId) {
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.userId = userId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ServiceCategory getCategorie() { return categorie; }
    public void setCategorie(ServiceCategory categorie) { this.categorie = categorie; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
