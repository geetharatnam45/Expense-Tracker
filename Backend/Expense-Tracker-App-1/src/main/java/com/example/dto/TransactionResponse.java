package com.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

import com.example.entity.Transaction;
import com.example.entity.TransactionType;

@Data
@AllArgsConstructor

public class TransactionResponse {
	
	private Long id;
    private String title;
    private Double amount;
    private String category;
    private LocalDate date;
    private TransactionType type;

    public static TransactionResponse fromEntity(Transaction t) {
        return new TransactionResponse(
                t.getId(), t.getTitle(), t.getAmount(),
                t.getCategory(), t.getDate(), t.getType());
    }

}
