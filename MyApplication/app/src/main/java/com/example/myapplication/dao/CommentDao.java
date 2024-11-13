package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    @Update
    void updateComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);
    @Query("SELECT * FROM comment WHERE id = :commentId LIMIT 1")
    Comment getCommentById(int commentId);

}
