package com.example.tourx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourx.R;
import com.example.tourx.activity.VacationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Navigate to VacationActivity
        Button toVacationActivity = findViewById(R.id.buttonGetStarted);
        toVacationActivity.setOnClickListener(v -> {
            Intent vacationIntent = new Intent(MainActivity.this, VacationActivity.class);
            startActivity(vacationIntent);
        });
    }
}