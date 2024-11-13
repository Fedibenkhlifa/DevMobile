package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Rating;
import com.example.myapplication.entity.Comment;

public class AddCommentActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText editTextComment;
    private Button buttonSubmitRating, buttonSubmitComment,buttonBack;
    private AppDatabase database;
    private int ratedUserId; // ID of the user being rated
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        ratingBar = findViewById(R.id.ratingBar);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmitRating = findViewById(R.id.buttonSubmitRating);
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);
        buttonBack = findViewById(R.id.buttonBack); // Initialize the back button

        ratedUserId = getIntent().getIntExtra("userId", -1);
        database = AppDatabase.getAppDatabase(getApplicationContext());
        sessionManager = new SessionManager(this);

        buttonSubmitRating.setOnClickListener(v -> submitRating());
        buttonSubmitComment.setOnClickListener(v -> submitComment());
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddCommentActivity.this, DetailsActivity.class);
            intent.putExtra("userId", ratedUserId); // Pass the rated user's ID back
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void submitRating() {
        int ratingValue = (int) ratingBar.getRating();
        int raterUserId = sessionManager.getUserId();

        // Vérifier si une note existe déjà pour cet utilisateur
        Rating existingRating = database.ratingDao().getRatingByUserIds(ratedUserId, raterUserId);
        if (existingRating != null) {
            existingRating.setRating(ratingValue);
            database.ratingDao().updateRating(existingRating);
            Toast.makeText(this, "Note mise à jour avec succès!", Toast.LENGTH_SHORT).show();
        } else {
            Rating newRating = new Rating(ratedUserId, raterUserId, ratingValue);
            database.ratingDao().insertRating(newRating);
            Toast.makeText(this, "Note ajoutée avec succès!", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitComment() {
        String commentText = editTextComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un commentaire.", Toast.LENGTH_SHORT).show();
            return;
        }

        int raterUserId = sessionManager.getUserId();
        Comment comment = new Comment(ratedUserId, raterUserId, commentText);
        database.commentDao().insertComment(comment);

        Toast.makeText(this, "Commentaire ajouté avec succès!", Toast.LENGTH_SHORT).show();
        editTextComment.setText(""); // Clear comment input
    }
}
