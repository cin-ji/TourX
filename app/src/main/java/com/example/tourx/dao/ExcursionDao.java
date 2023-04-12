package com.example.tourx.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tourx.entity.Excursion;

import java.util.List;

@Dao
public interface ExcursionDao {
    // Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    // Update
    @Update
    void update(Excursion excursion);

    // Delete
    @Delete
    void delete(Excursion excursion);

    // Queries
    @Query("SELECT * FROM excursions WHERE vacation_id = :vacationId")
    LiveData<List<Excursion>> getAllExcursions(int vacationId);
}
