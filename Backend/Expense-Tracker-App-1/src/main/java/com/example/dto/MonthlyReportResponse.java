package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor

public class MonthlyReportResponse {
	
	 private int year;
	    private int month;
	    private double totalIncome;
	    private double totalExpense;
	    private double savings;
	    private double savingsPercentage;
	    private Map<String, Double> expenseByCategory;

}
