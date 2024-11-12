package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Reservation;
import com.example.myapplication.entity.ServicePres;
import com.example.myapplication.entity.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReservationAdapterFront extends RecyclerView.Adapter<ReservationAdapterFront.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;
    private AppDatabase database;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ReservationAdapterFront(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
        this.database = AppDatabase.getAppDatabase(context);
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_reservationfront, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        // Get the service
        ServicePres service = database.servicePDao().getServiceById(reservation.getServiceId());

        // Get the user who made the reservation
        User user = database.userDao().getUserById(reservation.getUserId());

        // Set service name
        holder.textViewServiceName.setText(service != null ? service.getNom() : "Service inconnu");

        // Set reservation date
        holder.textViewReservationDate.setText("Date de réservation: " +
                dateFormat.format(reservation.getReservationDate()));

        // Set user name
        holder.textViewUserName.setText(user != null ? "Réservé par: " +
                user.getNom() + " " + user.getPrenom() : "Utilisateur inconnu");

        // Set status
        holder.textViewStatus.setText("Statut: " + reservation.getStatus());

        // Set click listener for delete icon
        holder.iconDeleteReservation.setOnClickListener(v -> {
            // Delete reservation from database
            database.reservationDao().deleteReservation(reservation);

            // Remove the item from the list and notify the adapter
            reservationList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, reservationList.size());

            Toast.makeText(context, "Réservation annulée", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewServiceName, textViewReservationDate, textViewUserName, textViewStatus;
        ImageView iconDeleteReservation; // Icon for deleting the reservation

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
            textViewReservationDate = itemView.findViewById(R.id.textViewReservationDate);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            iconDeleteReservation = itemView.findViewById(R.id.iconDeleteReservation); // Link to icon
        }
    }
}
