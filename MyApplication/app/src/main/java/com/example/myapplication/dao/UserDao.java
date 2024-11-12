package com.example.myapplication.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myapplication.entity.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);
    @Update
    void updateUser(User user);
    @Delete
    void deleteUser(User user);
    @Query("SELECT * FROM user WHERE id = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();
    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    User findUserByUsername(String username);
    @Query("SELECT * FROM user WHERE role = :role")
    List<User> getUsersByRole(String role);
}
