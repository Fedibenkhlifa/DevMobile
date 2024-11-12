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
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

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
            if (user.getImageUri() != null) {
                holder.imageViewUser.setImageURI(Uri.parse(user.getImageUri()));
            } else {
                holder.imageViewUser.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.textViewUserName.setText("Utilisateur inconnu");
            holder.imageViewUser.setImageResource(R.drawable.ic_launcher_foreground);
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
        ImageView imageViewUser;
        TextView textViewServiceName, textViewUserName;
        Button buttonServiceDetails;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            buttonServiceDetails = itemView.findViewById(R.id.buttonServiceDetails);
        }
    }
}
