package com.example.myapplication.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myapplication.entity.ServicePres;
import java.util.List;

@Dao
public interface ServicePDao {
    @Insert
    void insertService(ServicePres service);

    @Update
    void updateService(ServicePres service);

    @Delete
    void deleteService(ServicePres service);

    @Query("SELECT * FROM servicepr WHERE id = :serviceId")
    ServicePres getServiceById(int serviceId);

    @Query("SELECT * FROM servicepr WHERE userId = :userId")
    List<ServicePres> getServicesByUserId(int userId);

    @Query("SELECT * FROM servicepr")
    List<ServicePres> getAllServices();
}
