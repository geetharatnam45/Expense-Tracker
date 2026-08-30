package com.example.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.dto.TransactionRequest;
import com.example.dto.TransactionResponse;
import com.example.entity.TransactionType;
import com.example.service.TransactionService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@AuthenticationPrincipal UserDetails user,
                                                        @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.create(user.getUsername(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@AuthenticationPrincipal UserDetails user,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.update(user.getUsername(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails user, @PathVariable Long id) {
        transactionService.delete(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(transactionService.getAll(user.getUsername()));
    }

    // Search & filter: /api/transactions/search?type=EXPENSE&category=Food&startDate=2026-08-01&endDate=2026-08-31&keyword=coffee&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<Page<TransactionResponse>> search(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(transactionService.search(
                user.getUsername(), type, category, startDate, endDate, keyword, page, size));
    }
}
