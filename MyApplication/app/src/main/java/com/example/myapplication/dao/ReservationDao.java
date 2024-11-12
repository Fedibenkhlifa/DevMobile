package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myapplication.entity.Reservation;
import java.util.List;

@Dao
public interface ReservationDao {

    @Insert
    void insertReservation(Reservation reservation);

    @Update
    void updateReservation(Reservation reservation);

    @Query("SELECT * FROM reservations WHERE serviceId IN (:serviceIds)")
    List<Reservation> getReservationsByServiceIds(List<Integer> serviceIds);


    @Query("SELECT * FROM reservations WHERE userId = :userId")
    List<Reservation> getReservationsByUserId(int userId);

    @Query("SELECT * FROM reservations")
    List<Reservation> getAllReservations();
}
