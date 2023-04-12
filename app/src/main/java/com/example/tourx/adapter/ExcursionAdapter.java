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
import com.example.tourx.entity.Excursion;

import java.util.ArrayList;
import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> excursions = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1;

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }
    // Retrieve Vacation input fields and set their respective fields
    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();

        Excursion currentExcursion = excursions.get(currentPosition);
        holder.textViewExcursionTitle.setText(currentExcursion.getExcursionTitle());

        // Convert Date objects to formatted Strings
        String excursionDate = DateConverter.fromFormattedDate(currentExcursion.getExcursionDate());
        holder.textViewExcursionDate.setText(excursionDate);

        // Change the background color of the selected item
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        } else {
            // Reset the background color for unselected items
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        // Updates position based on selected item/Excursion
        holder.itemView.setOnClickListener(v -> {
            int newPosition = holder.getAdapterPosition();
            if (onItemClickListener != null && newPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(excursions.get(newPosition), excursions.get(newPosition).getVacationId());
                // Refresh the previous selected item
                notifyItemChanged(selectedPosition);
                // Update selected position to current selected item (true)
                selectedPosition = position;
                // Refresh the current selected item
                notifyItemChanged(selectedPosition);
            }
        });
    }
    // Return list of excursions
    @Override
    public int getItemCount() {
        return excursions.size();
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    // Setter for Excursions and updates(redraws) the list
    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }
    // Setter for OnItemLickListener method
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewExcursionTitle;
        private final TextView textViewExcursionDate;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExcursionTitle = itemView.findViewById(R.id.textViewExcursionTitle);
            textViewExcursionDate = itemView.findViewById(R.id.textViewExcursionDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    int vacationId = excursions.get(position).getVacationId();
                    onItemClickListener.onItemClick(excursions.get(position), vacationId);
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Excursion excursion, int vacationId);
    }
    public  void clearSelection() {
        int prevSelectedPosition = selectedPosition;
        selectedPosition = -1;
        notifyItemChanged(prevSelectedPosition);
    }
}
