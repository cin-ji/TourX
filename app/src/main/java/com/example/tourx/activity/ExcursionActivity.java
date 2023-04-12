package com.example.tourx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourx.R;
import com.example.tourx.adapter.ExcursionAdapter;
import com.example.tourx.database.DateConverter;
import com.example.tourx.entity.Excursion;
import com.example.tourx.entity.Vacation;
import com.example.tourx.viewmodel.ExcursionViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExcursionActivity extends AppCompatActivity {
    public static final String KEY_VacationID = "com.example.tourx.activity.KEY_VacationID";

    private Button toEditSelectedExcursion;
    private Excursion selectedExcursion = null;
    private ExcursionAdapter adapter;
    private List<Excursion> originalExcursions;
    private int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion);

        SearchView searchViewExcursions = findViewById(R.id.searchViewExcursions);
        searchViewExcursions.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        searchViewExcursions.setOnCloseListener(() -> {
            adapter.setExcursions(originalExcursions); // Restore the original list of vacations
            return false; // Return false to allow the default behavior (clearing the text and losing focus)
        });


        // Get Vacation selected
        Intent intent = getIntent();
        vacationId = intent.getIntExtra(KEY_VacationID, -1);
        String vacationStartDate = intent.getStringExtra(VacationDetailsActivity.KEY_StartDate);
        String vacationEndDate = intent.getStringExtra(VacationDetailsActivity.KEY_EndDate);

        // Initialize ExcursionAdapter
        adapter = new ExcursionAdapter();

        // Initialize Excursion ViewModel
        ExcursionViewModel excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);
        excursionViewModel.getAllExcursions(vacationId).observe(this, excursions -> {
            adapter.setExcursions(excursions);
            originalExcursions = new ArrayList<>(excursions); // Store the original list of excursions
        });
        // Navigate to ExcursionDetailsActivity to add an Excursion
        Button toExcursionDetailsActivity = findViewById(R.id.buttonAddAExcursion);
        toExcursionDetailsActivity.setOnClickListener(v -> {
            Intent excursionDetailsIntent = new Intent(ExcursionActivity.this, ExcursionDetailsActivity.class);
            excursionDetailsIntent.putExtra(KEY_VacationID, vacationId);
            excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_VacationStartDate, vacationStartDate);
            excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_VacationEndDate, vacationEndDate);
            startActivity(excursionDetailsIntent);
        });
        // Navigate to ExcursionDetailsActivity with selected Excursion to edit
        toEditSelectedExcursion = findViewById(R.id.buttonEditExcursion);
        toEditSelectedExcursion.setOnClickListener(v -> {
            // Retrieve all selected Vacation details and pass them to next Activity
            if (selectedExcursion != null) {
                // Fields to pass to the next Activity
                Intent excursionDetailsIntent = new Intent(ExcursionActivity.this, ExcursionDetailsActivity.class);
                excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_ExcursionID, selectedExcursion.getId());
                excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_ExcursionTitle, selectedExcursion.getExcursionTitle());
                excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_ExcursionDate, DateConverter.fromFormattedDate(selectedExcursion.getExcursionDate()));
                excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_VacationID, vacationId);
                excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_VacationStartDate, vacationStartDate);
                excursionDetailsIntent.putExtra(ExcursionDetailsActivity.KEY_VacationEndDate, vacationEndDate);
                startActivity(excursionDetailsIntent);
            } else {
                Toast.makeText(ExcursionActivity.this, "Select a Vacation", Toast.LENGTH_SHORT).show();
            }
        });
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExcursions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Set Excursion Adapter to Recycler View
        recyclerView.setAdapter(adapter);

        // Add divider for items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //  Observe LiveData from ViewModel and update the RecyclerView
        excursionViewModel.getAllExcursions(vacationId).observe(this, adapter::setExcursions);

        // Set item on listener once selected and enable Edit Excursion button
        adapter.setOnItemClickListener((excursion, excursionVacationId) -> {
            selectedExcursion = excursion;
            toEditSelectedExcursion.setEnabled(true);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.clearSelection();
            toEditSelectedExcursion.setEnabled(false);
            selectedExcursion = null;
        }
    }

    private void applyFilter(String query) {
        List<Excursion> filteredExcursions = new ArrayList<>();
        if (query.isEmpty()) {
            filteredExcursions.addAll(originalExcursions);
        } else {
            for (Excursion excursion : originalExcursions) {
                if (excursion.getExcursionTitle().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredExcursions.add(excursion);
                }
            }
        }
        adapter.setExcursions(filteredExcursions);
    }
}