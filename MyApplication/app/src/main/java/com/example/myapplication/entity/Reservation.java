package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import java.util.Date;

@Entity(
        tableName = "reservations",
        foreignKeys = {
                @ForeignKey(entity = ServicePres.class,
                        parentColumns = "id",
                        childColumns = "serviceId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int serviceId; // ID of the service being reserved
    private int userId; // ID of the user making the reservation
    private Date reservationDate; // Reservation date
    private String status = "non valide"; // Default value

    public Reservation(int serviceId, int userId, Date reservationDate) {
        this.serviceId = serviceId;
        this.userId = userId;
        this.reservationDate = reservationDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getReservationDate() { return reservationDate; }
    public void setReservationDate(Date reservationDate) { this.reservationDate = reservationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

