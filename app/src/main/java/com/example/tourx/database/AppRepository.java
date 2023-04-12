package com.example.tourx.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tourx.dao.ExcursionDao;
import com.example.tourx.dao.VacationDao;
import com.example.tourx.entity.Excursion;
import com.example.tourx.entity.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private final VacationDao vacationDao;
    private final ExcursionDao excursionDao;
    private final ExecutorService executorService;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Vacation
    public LiveData<List<Vacation>> getVacations() {
        return vacationDao.getAllVacations();
    }

    public void insert(Vacation vacation) {
        executorService.execute(() -> vacationDao.insert(vacation));
    }

    public void update(Vacation vacation) {
        executorService.execute(() -> vacationDao.update(vacation));
    }

    public void delete(Vacation vacation) {
        executorService.execute(() -> vacationDao.delete(vacation));
    }

    // Excursion
    public LiveData<List<Excursion>> getExcursions(int vacationId) {
        return excursionDao.getAllExcursions(vacationId);
    }

    public boolean insert(Excursion excursion) {
        try {
            executorService.execute(() -> excursionDao.insert(excursion));
            return true;
        } catch (Exception e) {
            Log.e("AppRepository", "Insertion failed: " + e.getMessage());
            return false;
        }
    }

    public void update(Excursion excursion) {
        executorService.execute(() -> excursionDao.update(excursion));
    }

    public void delete(Excursion excursion) {
        executorService.execute(() -> excursionDao.delete(excursion));
    }
}







