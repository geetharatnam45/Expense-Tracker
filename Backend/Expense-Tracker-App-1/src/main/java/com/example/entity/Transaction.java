package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank
	    @Column(nullable = false)
	    private String title;

	    @NotNull
	    @Positive
	    @Column(nullable = false)
	    private Double amount;

	    @NotBlank
	    @Column(nullable = false)
	    private String category;

	    @NotNull
	    @Column(nullable = false)
	    private LocalDate date;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private TransactionType type;

	    // Owning user - every transaction belongs to exactly one account
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;
}
