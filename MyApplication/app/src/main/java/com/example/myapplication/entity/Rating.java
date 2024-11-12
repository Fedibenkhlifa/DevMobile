package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rating")
public class Rating {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int ratedUserId; // ID of the user being rated
    private int raterUserId; // ID of the user making the rating
    private int rating; // Rating score
    private String comment; // Optional comment

    public Rating(int ratedUserId, int raterUserId, int rating, String comment) {
        this.ratedUserId = ratedUserId;
        this.raterUserId = raterUserId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRatedUserId() { return ratedUserId; }
    public void setRatedUserId(int ratedUserId) { this.ratedUserId = ratedUserId; }
    public int getRaterUserId() { return raterUserId; }
    public void setRaterUserId(int raterUserId) { this.raterUserId = raterUserId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
