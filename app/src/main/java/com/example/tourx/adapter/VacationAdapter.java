package com.example.tourx.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourx.R;
import com.example.tourx.database.DateConverter;
import com.example.tourx.entity.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacations = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1;

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_item, parent, false);
        return new VacationViewHolder(itemView);
    }
    // Retrieve Vacation input fields and set their respective fields
    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();

        Vacation currentVacation = vacations.get(currentPosition);
        holder.textViewVacationTitle.setText(currentVacation.getTitle());
        holder.textViewHotelName.setText(currentVacation.getHotelName());

        // Convert Date objects to formatted Strings
        String startDateString = DateConverter.fromFormattedDate(currentVacation.getStartDate());
        String endDateString = DateConverter.fromFormattedDate(currentVacation.getEndDate());
        // Set the text for the converted dates
        holder.textViewStartDate.setText(startDateString);
        holder.textViewEndDate.setText(endDateString);

        // Change the background color of the selected item
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        } else {
            // Reset the background color for unselected items
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        // Updates position based on selected item/Vacation
        holder.itemView.setOnClickListener(v -> {
            int newPosition = holder.getAdapterPosition();
            if (onItemClickListener != null && newPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(vacations.get(newPosition));
                // Refresh the previous selected item
                notifyItemChanged(selectedPosition);
                // Update selected position to current selected item (true)
                selectedPosition = position;
                // Refresh the current selected item
                notifyItemChanged(selectedPosition);
            }
        });
    }

    // Return list of vacations
    @Override
    public int getItemCount() {
        return vacations.size();
    }
    // Getter for Vacations (currently unused for now)
    public List<Vacation> getVacations() {
     return vacations;
    }
    // Setter for Vacations and updates(redraws) entire list
    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
        notifyDataSetChanged();
    }
    // Setter for OnItemLickListener method
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewVacationTitle;
        private final TextView textViewHotelName;
        private final TextView textViewStartDate;
        private final TextView textViewEndDate;

        // Initialize vacation items to textView
        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVacationTitle = itemView.findViewById(R.id.textViewVacationTitle);
            textViewHotelName = itemView.findViewById(R.id.textViewHotelName);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(vacations.get(position));
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Vacation vacation);
    }
    public void clearSelection() {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = -1;
        notifyItemChanged(previousSelectedPosition);
    }
}

