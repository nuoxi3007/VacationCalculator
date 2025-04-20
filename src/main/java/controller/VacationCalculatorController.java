package com.example.vacationcalculator.controller;

import com.example.vacationcalculator.service.VacationCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/vacation")
public class VacationCalculatorController {
    private static final Logger log = LoggerFactory.getLogger(VacationCalculatorController.class);
    private final VacationCalculatorService service;

    public VacationCalculatorController(VacationCalculatorService service) {
        this.service = service;
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateVacationPay(
            @RequestParam(name = "avgSalary") double avgSalary,
            @RequestParam(name = "days") int vacationDays,
            @RequestParam(name = "startDate", required = false) LocalDate startDate) {

        try {
            double amount = (startDate == null)
                    ? service.calculateSimple(avgSalary, vacationDays)
                    : service.calculateWithHolidays(avgSalary, vacationDays, startDate);

            return ResponseEntity.ok(new VacationResponse(
                    amount,
                    (startDate == null) ? "simple" : "with_holidays"
            ));
        } catch (Exception e) {
            log.error("Calculation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                    "Calculation failed",
                    e.getMessage()
            ));
        }
    }

    // DTO records
    record VacationResponse(double amount, String calculationType) {}
    record ErrorResponse(String error, String details) {}
}