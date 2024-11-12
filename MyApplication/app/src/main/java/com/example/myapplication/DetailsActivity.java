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
import com.example.myapplication.entity.Rating;
import com.example.myapplication.entity.User;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewNom, textViewPrenom, textViewRating;
    private ImageView imageViewProfile;
    private Button buttonAddComment;
    private AppDatabase database;
    private int userId;
    private RatingBar ratingBarAverage;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textViewNom = findViewById(R.id.textViewNom);
        textViewPrenom = findViewById(R.id.textViewPrenom);
        textViewRating = findViewById(R.id.textViewRating);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        ratingBarAverage = findViewById(R.id.ratingBarAverage);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);

        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

        userId = getIntent().getIntExtra("userId", -1);
        database = AppDatabase.getAppDatabase(getApplicationContext());

        loadUserDetails();

        // Launch AddCommentActivity and pass the userId
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, AddCommentActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserDetails(); // Reload details including comments
    }

    private void loadUserDetails() {
        User user = database.userDao().getUserById(userId);
        if (user != null) {
            textViewNom.setText(user.getNom());
            textViewPrenom.setText(user.getPrenom());

            // Load user-specific profile image
            String imagePath = user.getImageUri();
            if (imagePath != null && !imagePath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                imageViewProfile.setImageBitmap(bitmap);
            } else {
                imageViewProfile.setImageResource(android.R.color.transparent); // Leave blank if no image
            }

            // Load average rating and comments
            List<Rating> ratings = database.ratingDao().getRatingsByUserId(userId);
            float averageRating = database.ratingDao().getAverageRatingByUserId(userId);
            int ratingCount = database.ratingDao().getRatingCountByUserId(userId);

            ratingBarAverage.setRating(averageRating);
            textViewRating.setText(averageRating + " (" + ratingCount + " évaluations)");

            // Set up the CommentAdapter to display comments with rater's details
            commentAdapter = new CommentAdapter(this, ratings, database);
            recyclerViewComments.setAdapter(commentAdapter);
        }
    }
}
