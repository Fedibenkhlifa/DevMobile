package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

import java.io.File;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<ServicePres> serviceList;
    private List<User> userList;
    private Context context;

    public ServiceAdapter(Context context, List<ServicePres> serviceList, List<User> userList) {
        this.context = context;
        this.serviceList = serviceList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServicePres service = serviceList.get(position);

        // Set service name
        holder.textViewServiceName.setText(service.getNom());

        // Load service image if available
        if (holder.imageViewService != null) {
            if (service.getImagePath() != null) {
                File imageFile = new File(service.getImagePath());
                if (imageFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(service.getImagePath());
                    holder.imageViewService.setImageBitmap(bitmap);
                } else {
                    holder.imageViewService.setImageResource(R.drawable.ic_launcher_foreground); // Default image
                }
            } else {
                holder.imageViewService.setImageResource(R.drawable.ic_launcher_foreground); // Default image if no path
            }
        }

        // Find the user by userId in userList
        User user = null;
        for (User u : userList) {
            if (u.getId() == service.getUserId()) {
                user = u;
                break;
            }
        }

        if (user != null) {
            holder.textViewUserName.setText(user.getNom() + " " + user.getPrenom());

            // Set user image if available
            if (holder.imageViewUser != null) {
                if (user.getImageUri() != null) {
                    holder.imageViewUser.setImageURI(Uri.parse(user.getImageUri()));
                } else {
                    holder.imageViewUser.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            holder.textViewUserName.setText("Utilisateur inconnu");
            if (holder.imageViewUser != null) {
                holder.imageViewUser.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        // Set up the "Details" button click listener
        holder.buttonServiceDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceDetailsActivity.class);
            intent.putExtra("serviceId", service.getId()); // Pass the service ID to the details activity
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewService;
        ImageView imageViewUser;
        TextView textViewServiceName, textViewUserName;
        Button buttonServiceDetails;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewService = itemView.findViewById(R.id.imageViewService); // Ensure this ID is in item_service.xml
            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            buttonServiceDetails = itemView.findViewById(R.id.buttonServiceDetails);
        }
    }
}
