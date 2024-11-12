package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int ratedUserId; // ID of the user being commented on
    private int raterUserId; // ID of the user making the comment
    private String comment; // Comment text

    public Comment(int ratedUserId, int raterUserId, String comment) {
        this.ratedUserId = ratedUserId;
        this.raterUserId = raterUserId;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRatedUserId() { return ratedUserId; }
    public void setRatedUserId(int ratedUserId) { this.ratedUserId = ratedUserId; }
    public int getRaterUserId() { return raterUserId; }
    public void setRaterUserId(int raterUserId) { this.raterUserId = raterUserId; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
