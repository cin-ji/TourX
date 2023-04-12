package com.example.tourx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourx.R;
import com.example.tourx.adapter.VacationAdapter;
import com.example.tourx.database.DateConverter;
import com.example.tourx.entity.Vacation;
import com.example.tourx.viewmodel.VacationViewModel;

import java.util.ArrayList;
import java.util.List;

public class VacationActivity extends AppCompatActivity {
    private Button toEditSelectedVacation;
    private Vacation selectedVacation = null;
    private VacationAdapter adapter;
    private List<Vacation> originalVacations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation);

        SearchView searchViewVacations = findViewById(R.id.searchViewVacations);
        searchViewVacations.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilter(newText);
                return true;
            }
        });

        searchViewVacations.setOnCloseListener(() -> {
            adapter.setVacations(originalVacations); // Restore the original list of vacations
            return false; // Return false to allow the default behavior (clearing the text and losing focus)
        });
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewVacations);
        adapter = new VacationAdapter(); // Removed 'final' keyword here
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // Add divider for items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Initialize ViewModel
        VacationViewModel vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        vacationViewModel.getAllVacations().observe(this, adapter::setVacations);

        vacationViewModel.getAllVacations().observe(this, vacations -> {
            adapter.setVacations(vacations);
            originalVacations = new ArrayList<>(vacations); // Store the original list of vacations
        });

        // Set item on listener once selected and enable Edit button
        adapter.setOnItemClickListener(vacation -> {
            selectedVacation = vacation;
            toEditSelectedVacation.setEnabled(true);
        });

        // Navigate to VacationDetailsActivity to add a Vacation
        Button toVacationDetailsActivity = findViewById(R.id.buttonAddAVacation);
        toVacationDetailsActivity.setOnClickListener(v -> {
            Intent vacationDetailsIntent = new Intent(VacationActivity.this, VacationDetailsActivity.class);
            startActivity(vacationDetailsIntent);
        });
        // Navigate to VacationDetailsActivity to edit selected Vacation
        toEditSelectedVacation = findViewById(R.id.buttonEditVacation);
        toEditSelectedVacation.setOnClickListener(v -> {
            // Retrieve all selected Vacation details and pass them to next Activity
            if (selectedVacation != null) {
                // Fields to pass to the next Activity
                Intent vacationDetailsIntent = new Intent(VacationActivity.this, VacationDetailsActivity.class);
                vacationDetailsIntent.putExtra(VacationDetailsActivity.KEY_VacationID, selectedVacation.getId());
                vacationDetailsIntent.putExtra(VacationDetailsActivity.KEY_VacationTitle, selectedVacation.getTitle());
                vacationDetailsIntent.putExtra(VacationDetailsActivity.KEY_HotelName, selectedVacation.getHotelName());
                vacationDetailsIntent.putExtra(VacationDetailsActivity.KEY_StartDate, DateConverter.fromFormattedDate(selectedVacation.getStartDate()));
                vacationDetailsIntent.putExtra(VacationDetailsActivity.KEY_EndDate, DateConverter.fromFormattedDate(selectedVacation.getEndDate()));
                startActivity(vacationDetailsIntent);
            } else {
                Toast.makeText(VacationActivity.this, "Select a Vacation", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.clearSelection();
            toEditSelectedVacation.setEnabled(false);
            selectedVacation = null;
        }
    }
    private void applyFilter(String query) {
        List<Vacation> filteredVacations = new ArrayList<>();
        if (query.isEmpty()) {
            filteredVacations.addAll(originalVacations);
        } else {
            for (Vacation vacation : originalVacations) {
                if (vacation.getTitle().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredVacations.add(vacation);
                }
            }
        }
        adapter.setVacations(filteredVacations);
    }
}