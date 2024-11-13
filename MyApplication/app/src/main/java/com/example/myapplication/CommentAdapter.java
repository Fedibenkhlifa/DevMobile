package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private int loggedInUserId;

    public CommentAdapter(Context context, List<Comment> comments, AppDatabase database, int loggedInUserId) {
        this.context = context;
        this.comments = comments;
        this.database = database;
        this.loggedInUserId = loggedInUserId;
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
                // Set rater's profile image if available
                holder.imageViewRater.setImageBitmap(BitmapFactory.decodeFile(raterUser.getImageUri()));
            } else {
                holder.imageViewRater.setImageResource(android.R.color.transparent);
            }
        }

        // Show edit and delete icons only if the logged-in user is the author of the comment
        if (comment.getRaterUserId() == loggedInUserId) {
            holder.iconEditComment.setVisibility(View.VISIBLE);
            holder.iconDeleteComment.setVisibility(View.VISIBLE);

            holder.iconEditComment.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditCommentActivity.class);
                intent.putExtra("commentId", comment.getId());
                context.startActivity(intent);
            });

            holder.iconDeleteComment.setOnClickListener(v -> {
                database.commentDao().deleteComment(comment);
                Toast.makeText(context, "Comment deleted", Toast.LENGTH_SHORT).show();
                comments.remove(position);
                notifyItemRemoved(position);
            });
        } else {
            holder.iconEditComment.setVisibility(View.GONE);
            holder.iconDeleteComment.setVisibility(View.GONE);
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
        ImageView iconEditComment, iconDeleteComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRater = itemView.findViewById(R.id.imageViewRater);
            textViewRaterName = itemView.findViewById(R.id.textViewRaterName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            iconEditComment = itemView.findViewById(R.id.iconEditComment);
            iconDeleteComment = itemView.findViewById(R.id.iconDeleteComment);
        }
    }
}
