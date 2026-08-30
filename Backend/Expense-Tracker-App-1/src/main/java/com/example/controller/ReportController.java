package com.example.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.dto.MonthlyReportResponse;
import com.example.service.TransactionService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final TransactionService transactionService;

    // Defaults to the current month if year/month aren't supplied
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> monthly(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        LocalDate now = LocalDate.now();
        int y = year != null ? year : now.getYear();
        int m = month != null ? month : now.getMonthValue();

        return ResponseEntity.ok(transactionService.monthlyReport(user.getUsername(), y, m));
    }
}
