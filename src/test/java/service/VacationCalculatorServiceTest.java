package com.example.vacationcalculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class VacationCalculatorServiceTest {
    private final VacationCalculatorService service = new VacationCalculatorService();

    // Тестируем публичные методы
    @ParameterizedTest
    @CsvSource({
            "100000, 14, 47781.57",
            "50000, 7, 11945.39"
    })
    void calculateSimple_ValidInput_ReturnsCorrect(double salary, int days, double expected) {
        assertEquals(expected, service.calculateSimple(salary, days), 0.01);
    }

    @Test
    void calculateWithHolidays_NewYear_ExcludesHolidays() {
        double result = service.calculateWithHolidays(100000, 10,
                LocalDate.of(2024, 1, 1));
        assertEquals(27306.48, result, 0.01);
    }

    // Вместо тестирования isWorkDay() тестируем его через публичный метод
    @Test
    void calculateWithHolidays_Weekend_ReturnsCorrect() {
        double result = service.calculateWithHolidays(100000, 2,
                LocalDate.of(2024, 6, 1)); // 1 июня - суббота
        assertEquals(0.0, result, 0.01);
    }
}