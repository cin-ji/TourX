package com.example.tourx.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.tourx.R;
import com.example.tourx.database.DateConverter;
import com.example.tourx.entity.Excursion;
import com.example.tourx.entity.Vacation;
import com.example.tourx.viewmodel.VacationViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VacationDetailsActivity extends AppCompatActivity {
    // Vacation data keys to pass between activities
    public static final String KEY_VacationID = "com.example.tourx.activity.KEY_VacationID";
    public static final String KEY_VacationTitle = "com.example.app.KEY_VacationTitle";
    public static final String KEY_HotelName = "com.example.app.KEY_HotelName";
    public static final String KEY_StartDate = "com.example.app.KEY_StartDate";
    public static final String KEY_EndDate = "com.example.app.KEY_EndDate";

    public static final int START_ALARM_REQUEST_CODE = 100;
    public static final int END_ALARM_REQUEST_CODE = 200;
    private LiveData<List<Excursion>> excursions;


    private Button buttonVacationAlarm;
    private EditText editTextVacationTitle;
    private EditText editTextHotelName;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private VacationViewModel vacationViewModel;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        // Initialize Text fields
        TextView textViewScreenTitle = findViewById(R.id.textViewScreenTitle);
        editTextVacationTitle = findViewById(R.id.editTextVacationTitle);
        editTextHotelName = findViewById(R.id.editTextHotelName);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        // Initialize Buttons
        Button addVacation = findViewById(R.id.buttonAddVacation);
        Button saveVacation = findViewById(R.id.buttonSaveVacation);
        Button toExcursionActivity = findViewById(R.id.buttonAddAExcursion);
        Button deleteVacation = findViewById(R.id.buttonDeleteVacation);
        Button shareVacation = findViewById(R.id.buttonShareVacation);


        addTextWatchers();
        buttonVacationAlarm = findViewById(R.id.buttonVacationAlarm);
        buttonVacationAlarm.setEnabled(false);
        buttonVacationAlarm.setOnClickListener(v -> {
            // Get the start and end dates in milliseconds
            long startMillis = DateConverter.fromString(editTextStartDate.getText().toString()).getTime();
            long endMillis = DateConverter.fromString(editTextEndDate.getText().toString()).getTime();

            // Set the alarms
            setAlarm(startMillis, editTextVacationTitle.getText().toString(), true, START_ALARM_REQUEST_CODE);
            setAlarm(endMillis, editTextVacationTitle.getText().toString(), false, END_ALARM_REQUEST_CODE);
        });

        // Initialize ViewModel
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Call DatePicker function
        setupDatePicker();

        // Set Vacation details from selected Vacation
        Intent vacationIntent = getIntent();
        if (vacationIntent.hasExtra(KEY_VacationID)) {
            textViewScreenTitle.setText(vacationIntent.getStringExtra(KEY_VacationTitle));        // this will set the text title based on selected Vacation
            editTextVacationTitle.setText(vacationIntent.getStringExtra(KEY_VacationTitle));
            editTextHotelName.setText(vacationIntent.getStringExtra(KEY_HotelName));
            editTextStartDate.setText(vacationIntent.getStringExtra(KEY_StartDate));
            editTextEndDate.setText(vacationIntent.getStringExtra(KEY_EndDate));
            // Enable buttons if a Vacation is selected
            saveVacation.setEnabled(true);
            toExcursionActivity.setEnabled(true);
            deleteVacation.setEnabled(true);
            // No duplicates
            addVacation.setEnabled(false);
        } else {
            // Disable buttons if no Vacation is selected
            saveVacation.setEnabled(false);
            toExcursionActivity.setEnabled(false);
            deleteVacation.setEnabled(false);
        }
        // Share Button function
        shareVacation.setOnClickListener(v -> {
            String vacationTitle = editTextVacationTitle.getText().toString();
            String hotelName = editTextHotelName.getText().toString();
            String startDate = editTextStartDate.getText().toString();
            String endDate = editTextEndDate.getText().toString();

            String vacationDetails = "Vacation Title: " + vacationTitle + "\n" + "Hotel Name: " + hotelName + "\n" + "Start Date: " + startDate + "\n" + "End Date: " + endDate;

            shareVacationDetails(vacationDetails);
        });


        // Add Vacation Button function
        addVacation.setOnClickListener(v -> {
            String vacationTitle = editTextVacationTitle.getText().toString();
            String hotelName = editTextHotelName.getText().toString();
            String startDate = editTextStartDate.getText().toString();
            String endDate = editTextEndDate.getText().toString();
            // Implement validation if any fields are empty
            if (vacationTitle.trim().isEmpty() || hotelName.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Convert strings to Date objects
            Date startDateObj = DateConverter.fromString(startDate);
            Date endDateObj = DateConverter.fromString(endDate);

            // Validate end date is after the start date
            if (!isEndDateValid(startDateObj, endDateObj)) {
                Toast.makeText(this, "End Date should be after the Start Date", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create a new Vacation
            Vacation newVacation = new Vacation(vacationTitle, hotelName, startDateObj, endDateObj);

            vacationViewModel.insert(newVacation);
            Toast.makeText(this, "Added Vacation Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
        // Save Changes Button function
        saveVacation.setOnClickListener(v -> {
            if (vacationIntent.hasExtra(KEY_VacationID)) {
                int vacationId = vacationIntent.getIntExtra(KEY_VacationID, -1);
                String vacationTitle = editTextVacationTitle.getText().toString();
                String hotelName = editTextHotelName.getText().toString();
                String startDate = editTextStartDate.getText().toString();
                String endDate = editTextEndDate.getText().toString();
                // Implement validation if any fields are empty
                if (vacationTitle.trim().isEmpty() || hotelName.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
                    Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Convert strings to Date objects
                Date startDateObj = DateConverter.fromString(startDate);
                Date endDateObj = DateConverter.fromString(endDate);

                // Validate end date is after the start date
                if (!isEndDateValid(startDateObj, endDateObj)) {
                    Toast.makeText(this, "End Date should be after the Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Updates selected Vacation
                Vacation updatedVacation = new Vacation(vacationTitle, hotelName, startDateObj, endDateObj);
                updatedVacation.setId(vacationId);
                vacationViewModel.update(updatedVacation);

                Toast.makeText(this, "Updated Vacation Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // Delete Vacation Button function
        deleteVacation.setOnClickListener(v -> {
            if (vacationIntent.hasExtra(KEY_VacationID)) {
                int vacationId = vacationIntent.getIntExtra(KEY_VacationID, -1);

                excursions = vacationViewModel.getExcursions(vacationId);
                excursions.observe(this, excursions -> {
                    if (excursions != null && !excursions.isEmpty()) {
                        Toast.makeText(this, "Cannot be deleted. Vacation has associated excursions.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Deletes vacation
                        Vacation vacationToDelete = new Vacation("", "", null, null);
                        vacationToDelete.setId(vacationId);
                        vacationViewModel.delete(vacationToDelete);
                        Toast.makeText(this, "Deleted Vacation Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        // Navigate to Excursions Activity
        toExcursionActivity.setOnClickListener(v -> {
            if (vacationIntent.hasExtra(KEY_VacationID)) {
                int vacationId = vacationIntent.getIntExtra(KEY_VacationID, -1);
                String vacationStartDate = vacationIntent.getStringExtra(KEY_StartDate);
                String vacationEndDate = vacationIntent.getStringExtra(KEY_EndDate);
                Intent excursionIntent = new Intent(VacationDetailsActivity.this, ExcursionActivity.class);
                excursionIntent.putExtra(VacationDetailsActivity.KEY_VacationID, vacationId);
                excursionIntent.putExtra(VacationDetailsActivity.KEY_StartDate, vacationStartDate);
                excursionIntent.putExtra(VacationDetailsActivity.KEY_EndDate, vacationEndDate);
                Log.d("DEBUG", "Vacation ID: " + vacationId);

                startActivity(excursionIntent);
            }
        });
    }
    // Check if fields are empty then prompts
    private void checkDates() {
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();

        buttonVacationAlarm.setEnabled(!startDate.isEmpty() && !endDate.isEmpty());
    }

    private void setAlarm(long triggerAtMillis, String vacationTitle, boolean isStarting, int requestCode) {
        Intent intent = new Intent(this, VacationReceiver.class);
        intent.putExtra(VacationReceiver.KEY_VACATION_TITLE, vacationTitle);
        intent.putExtra(VacationReceiver.KEY_IS_STARTING, isStarting);

        // Add FLAG_IMMUTABLE
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

        Toast.makeText(this, "Vacation Alarm Set!", Toast.LENGTH_SHORT).show();
    }

    // Create DatePicker
    private void setupDatePicker() {
        // Set Start Date field on click (creates pop when field is selected)
        editTextStartDate.setOnClickListener(v -> {
            // Create calendar
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetailsActivity.this, startDateSetListener, year, month, day);
            showDatePickerDialog(editTextStartDate, datePickerDialog);
        });
        // Populates Start Date field to selected date from Date Picker
        startDateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = month + "/" + day + "/" + year;
            editTextStartDate.setText(date);
        };
        // Set End Date field on click (creates pop when field is selected)
        editTextEndDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetailsActivity.this, endDateSetListener, year, month, day);
            showDatePickerDialog(editTextEndDate, datePickerDialog);
        });
        // Populates End Date field to selected date from Date Picker
        endDateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = month + "/" + day + "/" + year;
            editTextEndDate.setText(date);
        };
    }

    private void showDatePickerDialog(EditText editText, DatePickerDialog datePickerDialog) {
        datePickerDialog.setOnDismissListener(dialog -> checkDates());

        datePickerDialog.show();
    }

    private boolean isEndDateValid(Date startDate, Date endDate) {
        return endDate.after(startDate);
    }

    private void shareVacationDetails(String vacationDetails) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
        shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    // Calls checkdates to check if fields have changed or empty
    private void addTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkDates();
            }
        };

        editTextStartDate.addTextChangedListener(textWatcher);
        editTextEndDate.addTextChangedListener(textWatcher);
    }
}