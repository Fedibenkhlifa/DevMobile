package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entity.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insertComment(Comment comment);

    @Query("SELECT * FROM comment WHERE ratedUserId = :ratedUserId")
    List<Comment> getCommentsByRatedUserId(int ratedUserId);
    @Query("SELECT * FROM comment WHERE ratedUserId = :userId")
    List<Comment> getCommentsByUserId(int userId);
}
