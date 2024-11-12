package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Rating;

public class AddCommentActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText editTextComment;
    private Button buttonSubmitComment;
    private AppDatabase database;
    private int ratedUserId; // ID of the user being rated
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        ratingBar = findViewById(R.id.ratingBar);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);

        ratedUserId = getIntent().getIntExtra("userId", -1); // The user being rated
        database = AppDatabase.getAppDatabase(getApplicationContext());
        sessionManager = new SessionManager(this);

        buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });
    }

    private void submitComment() {
        int ratingValue = (int) ratingBar.getRating();
        String commentText = editTextComment.getText().toString().trim();

        if (ratingValue == 0 || commentText.isEmpty()) {
            Toast.makeText(this, "Veuillez ajouter une note et un commentaire.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the raterUserId from SharedPreferences
        int raterUserId = sessionManager.getUserId(); // ID of the user who is submitting the rating

        Rating rating = new Rating(ratedUserId, raterUserId, ratingValue, commentText);
        database.ratingDao().insertRating(rating);

        Toast.makeText(this, "Commentaire ajouté avec succès!", Toast.LENGTH_SHORT).show();
        finish();
    }


}
