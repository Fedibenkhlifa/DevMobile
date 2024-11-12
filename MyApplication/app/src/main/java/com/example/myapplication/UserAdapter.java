package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.User;

import java.io.File;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewNom.setText(user.getNom());
        holder.textViewRole.setText(user.getRole().toString());

        // Charger l'image depuis le chemin local
        if (user.getImageUri() != null) {
            File imageFile = new File(user.getImageUri());
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(user.getImageUri());
                holder.imageViewUser.setImageBitmap(bitmap);
            } else {
                holder.imageViewUser.setImageResource(R.drawable.ic_launcher_foreground); // Image par défaut
            }
        } else {
            holder.imageViewUser.setImageResource(R.drawable.ic_launcher_foreground); // Image par défaut si pas d'image
        }

        // Configurer le bouton "Détails"
        holder.buttonDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("userId", user.getId()); // Passez l'ID de l'utilisateur à l'activité DetailsActivity
            context.startActivity(intent);
        });

        // Configurer le bouton "Afficher les services"
        holder.buttonViewServices.setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayServicesActivity.class);
            intent.putExtra("userId", user.getId()); // Passez l'ID de l'utilisateur pour filtrer ses services
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewUser;
        TextView textViewNom, textViewRole;
        Button buttonDetails, buttonViewServices;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            textViewNom = itemView.findViewById(R.id.textViewNom);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            buttonDetails = itemView.findViewById(R.id.buttonDetails); // Assurez-vous que ce bouton existe dans item_user.xml
            buttonViewServices = itemView.findViewById(R.id.buttonViewServices); // Nouveau bouton pour afficher les services
        }
    }
}
