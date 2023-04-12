package com.example.tourx.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tourx.database.AppRepository;
import com.example.tourx.entity.Excursion;
import com.example.tourx.entity.Vacation;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    private final AppRepository appRepository;

    public VacationViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return appRepository.getVacations();
    }

    public LiveData<List<Excursion>> getExcursions(int vacationId) {
        return appRepository.getExcursions(vacationId);
    }
    public void insert(Vacation vacation) {
        appRepository.insert(vacation);
    }

    public void update(Vacation vacation) {
        appRepository.update(vacation);
    }

    public void delete(Vacation vacation) {
        appRepository.delete(vacation);
    }
}