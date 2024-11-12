package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.ServicePres;

import java.util.List;

public class ServiceAdapterPr extends RecyclerView.Adapter<ServiceAdapterPr.ServiceViewHolder> {

    private List<ServicePres> serviceList;
    private Context context;
    private AppDatabase database;

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

        holder.textViewServiceName.setText(service.getNom());
        holder.textViewServiceDescription.setText(service.getDescription());
        holder.textViewServicePrice.setText(String.format("$%.2f", service.getPrix()));

        // Détails Button
        holder.buttonServiceDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceDetailsActivity.class);
            intent.putExtra("serviceId", service.getId());
            context.startActivity(intent);
        });

        // Modifier Button
        holder.buttonEditService.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditServiceActivity.class);
            intent.putExtra("serviceId", service.getId());

            // Check if the context is an instance of UserInterfaceActivity to use startActivityForResult
            if (context instanceof UserInterfaceActivity) {
                ((UserInterfaceActivity) context).startActivityForResult(intent, 100); // 100 is a request code
            }
        });


        // Supprimer Button
        holder.buttonDeleteService.setOnClickListener(v -> {
            database.servicePDao().deleteService(service);
            serviceList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, serviceList.size());
            Toast.makeText(context, "Service supprimé", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewService;
        TextView textViewServiceName, textViewServiceDescription, textViewServicePrice;
        Button buttonServiceDetails, buttonEditService, buttonDeleteService;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
            textViewServiceDescription = itemView.findViewById(R.id.textViewServiceDescription);
            textViewServicePrice = itemView.findViewById(R.id.textViewServicePrice);
            buttonServiceDetails = itemView.findViewById(R.id.buttonServiceDetails);
            buttonEditService = itemView.findViewById(R.id.buttonEditService);
            buttonDeleteService = itemView.findViewById(R.id.buttonDeleteService);
        }
    }
}
