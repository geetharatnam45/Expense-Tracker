package com.example.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;
import com.example.entity.TransactionType;

@Data

public class TransactionRequest {
	
	 @NotBlank
	    private String title;

	    @NotNull @Positive
	    private Double amount;

	    @NotBlank
	    private String category;

	    @NotNull
	    private LocalDate date;

	    @NotNull
	    private TransactionType type;

}
