package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Comment;

public class EditCommentActivity extends AppCompatActivity {

    private EditText editTextComment;
    private Button buttonSaveComment, buttonCancelEdit,buttonBack;
    private AppDatabase database;
    private int commentId;
    private int ratedUserId; // ID of the user being rated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        editTextComment = findViewById(R.id.editTextComment);
        buttonSaveComment = findViewById(R.id.buttonSaveComment);
        buttonCancelEdit = findViewById(R.id.buttonCancelEdit);
        buttonBack = findViewById(R.id.buttonBack); // Initialize the back button

        database = AppDatabase.getAppDatabase(getApplicationContext());
        commentId = getIntent().getIntExtra("commentId", -1);

        loadCommentDetails();

        buttonSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateComment();
            }
        });

        buttonCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(EditCommentActivity.this, DetailsActivity.class);
            intent.putExtra("userId", ratedUserId); // Pass the rated user's ID back
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void loadCommentDetails() {
        Comment comment = database.commentDao().getCommentById(commentId);
        if (comment != null) {
            editTextComment.setText(comment.getComment());
        } else {
            Toast.makeText(this, "Comment not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateComment() {
        String updatedCommentText = editTextComment.getText().toString().trim();
        if (updatedCommentText.isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment comment = database.commentDao().getCommentById(commentId);
        if (comment != null) {
            comment.setComment(updatedCommentText);
            database.commentDao().updateComment(comment);
            Toast.makeText(this, "Comment updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating comment", Toast.LENGTH_SHORT).show();
        }
    }
}
