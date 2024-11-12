package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entity.Rating;

import java.util.List;

@Dao
public interface RatingDao {
    @Insert
    void insertRating(Rating rating);

    // Get all ratings given to a specific user (ratedUserId)
    @Query("SELECT * FROM rating WHERE ratedUserId = :ratedUserId")
    List<Rating> getRatingsByUserId(int ratedUserId);

    // Calculate the average rating for a specific user (ratedUserId)
    @Query("SELECT AVG(rating) FROM rating WHERE ratedUserId = :ratedUserId")
    float getAverageRatingByUserId(int ratedUserId);

    // Count the number of ratings for a specific user (ratedUserId)
    @Query("SELECT COUNT(rating) FROM rating WHERE ratedUserId = :ratedUserId")
    int getRatingCountByUserId(int ratedUserId);
}
