package com.example.tourx;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.tourx.database.DateConverter;
import com.example.tourx.entity.Excursion;
import com.example.tourx.entity.Vacation;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    // Test insert function for Vacation
    @Test
    public void vacationEntityTest() {
        String title = "Vacation 1";
        String hotelName = "Hotel Test";
        Date startDate = new Date(2023, 1, 1);
        Date endDate = new Date(2023, 1, 7);

        Vacation vacation = new Vacation(title, hotelName, startDate, endDate);

        assertEquals(title, vacation.getTitle());
        assertEquals(hotelName, vacation.getHotelName());
        assertEquals(startDate, vacation.getStartDate());
        assertEquals(endDate, vacation.getEndDate());
    }
    // Test insert function for Excursion
    @Test
    public void excursionEntityTest() {
        String excursionTitle = "Excursion 1";
        Date excursionDate = new Date(2023, 1, 2);
        int vacationId = 1;

        Excursion excursion = new Excursion(excursionTitle, excursionDate, vacationId);

        assertEquals(excursionTitle, excursion.getExcursionTitle());
        assertEquals(excursionDate, excursion.getExcursionDate());
        assertEquals(vacationId, excursion.getVacationId());
    }
    // Test date converter
    @Test
    public void dateConverterTest() {
        String dateString = "04/12/2023";
        Date date = DateConverter.fromString(dateString);
        String convertedDateString = DateConverter.fromFormattedDate(date);

        assertEquals(dateString, convertedDateString);
    }
    public static boolean isEndDateValid(Date startDate, Date endDate) {
        return endDate.after(startDate);
    }
    // Test if end date is valid based on data converter test
//    @Test
//    public void isEndDateValidTest() {
//        Date startDate = DateConverter.fromString("04/12/2023");
//        Date endDateValid = DateConverter.fromString("04/14/2023");
//        Date endDateInvalid = DateConverter.fromString("04/10/2023");
//
//        assertTrue(VacationDetailsActivity.isEndDateValid(startDate, endDateValid));
//        assertFalse(VacationDetailsActivity.isEndDateValid(startDate, endDateInvalid));
//    }
    // Test if edit text fields are empty
    @Test
    public void fieldsNotEmpty() {
        String vacationTitle = "My Vacation";
        String hotelName = "Hotel";
        String startDate = "04/12/2023";
        String endDate = "";

        assertFalse(vacationTitle.trim().isEmpty());
        assertFalse(hotelName.trim().isEmpty());
        assertFalse(startDate.trim().isEmpty());
        assertFalse(endDate.trim().isEmpty());
    }
}