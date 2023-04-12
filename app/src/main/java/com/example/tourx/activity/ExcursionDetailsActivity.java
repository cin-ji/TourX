package com.example.tourx.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tourx.R;
import com.example.tourx.database.DateConverter;
import com.example.tourx.entity.Excursion;
import com.example.tourx.viewmodel.ExcursionViewModel;

import java.util.Calendar;
import java.util.Date;

public class ExcursionDetailsActivity extends AppCompatActivity {

    public static final String KEY_ExcursionID = "com.example.tourx.activity.KEY_ExcursionID";
    public static final String KEY_ExcursionTitle = "com.example.tourx.activity.KEY_ExcursionTitle";
    public static final String KEY_ExcursionDate = "com.example.tourx.activity.KEY_ExcursionDate";
    public static final String KEY_VacationID = "com.example.tourx.activity.KEY_VacationID";
    public static final String KEY_VacationStartDate = "com.example.tourx.activity.KEY_VacationStartDate";
    public static final String KEY_VacationEndDate = "com.example.tourx.activity.KEY_VacationEndDate";


    private EditText editTextExcursionTitle;
    private EditText editTextExcursionDate;
    private ExcursionViewModel excursionViewModel;
    private DatePickerDialog.OnDateSetListener excursionDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        Button buttonExcursionAlarm = findViewById(R.id.buttonExcursionAlarm);
        buttonExcursionAlarm.setOnClickListener(v -> {
            String excursionTitle = editTextExcursionTitle.getText().toString();
            String excursionDate = editTextExcursionDate.getText().toString();
            if (!excursionDate.isEmpty()) {
                Date date = DateConverter.fromString(excursionDate);
                setExcursionAlarm(date.getTime(), excursionTitle);
            } else {
                Toast.makeText(this, "Please select an excursion date", Toast.LENGTH_SHORT).show();
            }
        });

        // Get vacation from Intent (selected Vacation)
        Intent intent = getIntent();
        int vacationId = intent.getIntExtra(KEY_VacationID, -1);
        Log.d("DEBUG", "Vacation ID: " + vacationId);


        // Initialize Text fields
        TextView textViewScreenTitle = findViewById(R.id.textViewScreenTitle);
        editTextExcursionTitle = findViewById(R.id.editTextExcursionTitle);
        editTextExcursionDate = findViewById(R.id.editTextExcursionDate);
        // Initialize Buttons
        Button addExcursion = findViewById(R.id.buttonAddExcursion);
        Button saveExcursion = findViewById(R.id.buttonSaveExcursion);
        Button cancel = findViewById(R.id.buttonCancelExcursion);
        Button deleteExcursion = findViewById(R.id.buttonDeleteExcursion);

        Date vacationStartDate = DateConverter.fromString(intent.getStringExtra(KEY_VacationStartDate));
        Date vacationEndDate = DateConverter.fromString(intent.getStringExtra(KEY_VacationEndDate));

        // Initialize ViewModel
        excursionViewModel = new ViewModelProvider(this).get(ExcursionViewModel.class);

        // Call DatePicker function
        setupDatePicker();


        // Get excursionId from ExcursionIntent (selected Excursion)
//        int excursionId = intent.getIntExtra(KEY_ExcursionID, -1);

        Intent excursionIntent = getIntent();
        if (excursionIntent.hasExtra(KEY_ExcursionID)) {
            textViewScreenTitle.setText(excursionIntent.getStringExtra(KEY_ExcursionTitle));
            editTextExcursionTitle.setText(excursionIntent.getStringExtra(KEY_ExcursionTitle));
            editTextExcursionDate.setText(excursionIntent.getStringExtra(KEY_ExcursionDate));
            // Enables button if an Excursion is selected
            saveExcursion.setEnabled(true);
            deleteExcursion.setEnabled(true);
            // No duplicates
            addExcursion.setEnabled(false);
        } else {
            // Disable buttons if no Excursion is selected
            saveExcursion.setEnabled(false);
            deleteExcursion.setEnabled(false);
        }
        // Add Excursion Button function
        addExcursion.setOnClickListener(v -> {
            String excursionTitle = editTextExcursionTitle.getText().toString();
            String excursionDate = editTextExcursionDate.getText().toString();


            // Implement validation if any fields are empty
            if (excursionTitle.trim().isEmpty() || excursionDate.trim().isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Convert strings to Date objects
            Date excursionDateObj = DateConverter.fromString(excursionDate);
            if (!isExcursionDateValid(excursionDateObj, vacationStartDate, vacationEndDate)) {
                Toast.makeText(this, "Excursion date should be within the Vacation Start and End Dates", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create a new Excursion
            Excursion newExcursion = new Excursion(excursionTitle, excursionDateObj, vacationId);
            // Set alarm for the excursion date
            excursionViewModel.insert(newExcursion);

            Toast.makeText(this, "Added Excursion Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
        // Save Excursion button function
        saveExcursion.setOnClickListener(v -> {
            if (excursionIntent.hasExtra(KEY_ExcursionID)) {
                int excursionId = excursionIntent.getIntExtra(KEY_ExcursionID, -1);
                String excursionTitle = editTextExcursionTitle.getText().toString();
                String excursionDate = editTextExcursionDate.getText().toString();
                // Implement validation if any fields are empty
                if (excursionTitle.trim().isEmpty() || excursionDate.trim().isEmpty()) {
                    Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Convert strings to Date objects
                Date excursionDateObj = DateConverter.fromString(excursionDate);
                Log.d("ExcursionDetails", "Vacation ID: " + vacationId);
                Log.d("ExcursionDetails", "Excursion ID: " + excursionId);

                if (!isExcursionDateValid(excursionDateObj, vacationStartDate, vacationEndDate)) {
                    Toast.makeText(this, "Excursion date should be within the Vacation Start and End Dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new Excursion
                Excursion updatedExcursion = new Excursion(excursionTitle, excursionDateObj, vacationId);
                // Set alarm for the excursion date

                updatedExcursion.setId(excursionId);
                excursionViewModel.update(updatedExcursion);
                Toast.makeText(this, "Updated Excursion Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // Delete Excursion button function
        deleteExcursion.setOnClickListener(v -> {
            if (excursionIntent.hasExtra(KEY_ExcursionID)) {
                int excursionId = excursionIntent.getIntExtra(KEY_ExcursionID, -1);
                String excursionTitle = editTextExcursionTitle.getText().toString();
                String excursionDate = editTextExcursionDate.getText().toString();

                // Convert strings to Date objects
                Date excursionDateObj = DateConverter.fromString(excursionDate);

                // Create a new Excursion
                Excursion excursionToDelete = new Excursion(excursionTitle, excursionDateObj, vacationId);
                excursionToDelete.setId(excursionId);
                excursionViewModel.delete(excursionToDelete);
                Toast.makeText(this, "Deleted Excursion Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // Cancel Button function
        cancel.setOnClickListener(v -> finish());
    }

    // Create DatePicker
    private void setupDatePicker() {
        // Set Start Date field on click (creates pop when field is selected)
        editTextExcursionDate.setOnClickListener(v -> {
            // Create calendar
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExcursionDetailsActivity.this,
                    excursionDateListener,
                    year, month, day
            );
            datePickerDialog.show();
        });
        // Populates Start Date field to selected date from Date Picker
        excursionDateListener = (view, year, month, day) -> {
            month = month + 1;
            String date = month + "/" + day + "/" + year;
            editTextExcursionDate.setText(date);
        };

    }

    private boolean isExcursionDateValid(Date excursionDate, Date vacationStartDate, Date vacationEndDate) {
        return excursionDate.compareTo(vacationStartDate) >= 0 && excursionDate.compareTo(vacationEndDate) <= 0;
    }
    private void setExcursionAlarm(long triggerAtMillis, String excursionTitle) {
        Intent intent = new Intent(this, ExcursionReceiver.class);
        intent.putExtra(ExcursionReceiver.KEY_EXCURSION_TITLE, excursionTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

        Toast.makeText(this, "Excursion Alarm Set!", Toast.LENGTH_SHORT).show();
    }
}