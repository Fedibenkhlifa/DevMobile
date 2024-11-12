package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.User;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private AppDatabase database;
    private Context context;

    public CommentAdapter(Context context, List<Comment> comments, AppDatabase database) {
        this.context = context;
        this.comments = comments;
        this.database = database;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        User raterUser = database.userDao().getUserById(comment.getRaterUserId());

        holder.textViewComment.setText(comment.getComment());

        if (raterUser != null) {
            holder.textViewRaterName.setText(raterUser.getNom());

            if (raterUser.getImageUri() != null && !raterUser.getImageUri().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(raterUser.getImageUri());
                holder.imageViewRater.setImageBitmap(bitmap);
            } else {
                holder.imageViewRater.setImageResource(android.R.color.transparent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewRater;
        TextView textViewRaterName;
        TextView textViewComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRater = itemView.findViewById(R.id.imageViewRater);
            textViewRaterName = itemView.findViewById(R.id.textViewRaterName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
        }
    }
}
