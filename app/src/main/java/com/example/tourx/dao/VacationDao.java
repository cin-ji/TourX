package com.example.tourx.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tourx.entity.Vacation;

import java.util.List;

@Dao
public interface VacationDao {
    // Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    // Update
    @Update
    void update(Vacation vacation);

    // Delete
    @Delete
    void delete(Vacation vacation);

    // Queries
    @Query("SELECT * FROM vacations")
    LiveData<List<Vacation>> getAllVacations();
}
