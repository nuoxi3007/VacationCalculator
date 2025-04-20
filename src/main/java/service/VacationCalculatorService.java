package com.example.vacationcalculator.service;

import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import static java.time.Month.*;

@Service
public class VacationCalculatorService {
    private static final double AVERAGE_WORKING_DAYS_PER_MONTH = 29.3;
    private static final Set<LocalDate> HOLIDAYS = Set.of(
            // Январь
            LocalDate.of(2024, JANUARY, 1), LocalDate.of(2024, JANUARY, 2),
            LocalDate.of(2024, JANUARY, 3), LocalDate.of(2024, JANUARY, 4),
            LocalDate.of(2024, JANUARY, 5), LocalDate.of(2024, JANUARY, 6),
            LocalDate.of(2024, JANUARY, 7), LocalDate.of(2024, JANUARY, 8),

            // Февраль-Декабрь
            LocalDate.of(2024, FEBRUARY, 23),
            LocalDate.of(2024, MARCH, 8),
            LocalDate.of(2024, MAY, 1), LocalDate.of(2024, MAY, 9),
            LocalDate.of(2024, JUNE, 12),
            LocalDate.of(2024, NOVEMBER, 4)
    );

    public double calculateSimple(double avgSalary, int vacationDays) {
        validateInput(avgSalary, vacationDays);
        return round(avgSalary / AVERAGE_WORKING_DAYS_PER_MONTH * vacationDays);
    }

    public double calculateWithHolidays(double avgSalary, int vacationDays, LocalDate startDate) {
        validateInput(avgSalary, vacationDays);
        Objects.requireNonNull(startDate, "Start date cannot be null");

        int workingDays = countWorkingDays(startDate, vacationDays);
        return calculateSimple(avgSalary, workingDays);
    }

    private int countWorkingDays(LocalDate startDate, int days) {
        int workingDays = 0;
        LocalDate currentDate = startDate;

        for (int i = 0; i < days; i++) {
            if (isWorkDay(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return workingDays;
    }

    private boolean isWorkDay(LocalDate date) {
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                HOLIDAYS.contains(date));
    }

    private void validateInput(double salary, int days) {
        if (salary < 0 || days < 0) {
            throw new IllegalArgumentException("Salary and vacation days must be positive");
        }
    }

    private double round(double value) {
        return Math.round(value * 100) / 100.0;
    }
}