package com.example.tourx.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "excursions", foreignKeys = @ForeignKey(entity = Vacation.class, parentColumns = "id", childColumns = "vacation_id", onDelete = ForeignKey.CASCADE))
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String excursionTitle;
    private Date excursionDate;
    @ColumnInfo(name = "vacation_id")
    private int vacationId;

    // Constructor

    public Excursion(String excursionTitle, Date excursionDate, int vacationId) {
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationId = vacationId;
    }

    // Getter and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public Date getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(Date excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}
