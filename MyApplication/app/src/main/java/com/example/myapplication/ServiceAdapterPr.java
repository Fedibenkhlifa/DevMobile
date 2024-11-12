package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

import java.util.List;

public class ServiceAdapterPr extends RecyclerView.Adapter<ServiceAdapterPr.ServiceViewHolder> {

    private List<ServicePres> serviceList;
    private Context context;
    private AppDatabase database; // Database instance to fetch user data

    public ServiceAdapterPr(Context context, List<ServicePres> serviceList, AppDatabase database) {
        this.context = context;
        this.serviceList = serviceList;
        this.database = database;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_pr, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServicePres service = serviceList.get(position);

        // Set service details
        holder.textViewServiceName.setText(service.getNom());
        holder.textViewServiceDescription.setText(service.getDescription());
        holder.textViewServicePrice.setText(String.format("$%.2f", service.getPrix()));

        // Fetch and display user data (e.g., profile image) associated with the service
        User user = database.userDao().getUserById(service.getUserId()); // Get user who created the service

        if (user != null && user.getImageUri() != null && !user.getImageUri().isEmpty()) {
            holder.imageViewService.setImageURI(Uri.parse(user.getImageUri()));
        } else {
            holder.imageViewService.setImageResource(R.drawable.ic_launcher_foreground); // Default image if no URI
        }
        // Set up the "Details" button click listener
        holder.buttonServiceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceDetailsActivity.class);
                intent.putExtra("serviceId", service.getId()); // Pass the service ID to the details activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewService;
        TextView textViewServiceName, textViewServiceDescription, textViewServicePrice;
        Button buttonServiceDetails;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewService = itemView.findViewById(R.id.imageViewService);
            textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
            textViewServiceDescription = itemView.findViewById(R.id.textViewServiceDescription);
            textViewServicePrice = itemView.findViewById(R.id.textViewServicePrice);
            buttonServiceDetails = itemView.findViewById(R.id.buttonServiceDetails);

        }
    }
}
