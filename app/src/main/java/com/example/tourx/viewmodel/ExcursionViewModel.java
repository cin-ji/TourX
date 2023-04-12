package com.example.tourx.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tourx.database.AppRepository;
import com.example.tourx.entity.Excursion;

import java.util.List;

public class ExcursionViewModel extends AndroidViewModel {
    private final AppRepository appRepository;

    public ExcursionViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public LiveData<List<Excursion>> getAllExcursions(int vacationId) {
        return appRepository.getExcursions(vacationId);
    }

    public void insert(Excursion excursion) {
        appRepository.insert(excursion);
    }

    public void update(Excursion excursion) {
        appRepository.update(excursion);
    }

    public void delete(Excursion excursion) {
        appRepository.delete(excursion);
    }
}