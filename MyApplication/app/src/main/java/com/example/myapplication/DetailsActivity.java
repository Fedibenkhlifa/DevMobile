package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.Rating;
import com.example.myapplication.entity.User;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewNom, textViewPrenom, textViewRating;
    private ImageView imageViewProfile;
    private Button buttonAddComment,buttonBack;
    private AppDatabase database;
    private int userId;
    private RatingBar ratingBarAverage;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private int loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize SessionManager or any other method to get the logged-in user ID
        SessionManager sessionManager = new SessionManager(this);
        loggedInUserId = sessionManager.getUserId(); // Assuming this method returns the user ID

        // Initialize views, database, etc.
        textViewNom = findViewById(R.id.textViewNom);
        textViewPrenom = findViewById(R.id.textViewPrenom);
        textViewRating = findViewById(R.id.textViewRating);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        ratingBarAverage = findViewById(R.id.ratingBarAverage);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        buttonBack = findViewById(R.id.buttonBack); // Initialize the back button

        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

        userId = getIntent().getIntExtra("userId", -1);
        database = AppDatabase.getAppDatabase(getApplicationContext());

        loadUserDetails();
        buttonBack.setOnClickListener(v -> finish()); // Closes the current activity

        buttonAddComment.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsActivity.this, AddCommentActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserDetails(); // Recharger les détails, y compris les évaluations et commentaires
    }

    protected void loadUserDetails() {
        User user = database.userDao().getUserById(userId);
        if (user != null) {
            textViewNom.setText(user.getNom());
            textViewPrenom.setText(user.getPrenom());

            String imagePath = user.getImageUri();
            if (imagePath != null && !imagePath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                imageViewProfile.setImageBitmap(bitmap);
            } else {
                imageViewProfile.setImageResource(android.R.color.transparent);
            }

            List<Rating> ratings = database.ratingDao().getRatingsByUserId(userId);
            float averageRating = database.ratingDao().getAverageRatingByUserId(userId);
            int ratingCount = database.ratingDao().getRatingCountByUserId(userId);

            ratingBarAverage.setRating(averageRating);
            textViewRating.setText(String.format("%.1f (%d évaluations)", averageRating, ratingCount));

            List<Comment> comments = database.commentDao().getCommentsByUserId(userId);
            commentAdapter = new CommentAdapter(this, comments, database, loggedInUserId); // Pass loggedInUserId here
            recyclerViewComments.setAdapter(commentAdapter);
        }
    }

}
