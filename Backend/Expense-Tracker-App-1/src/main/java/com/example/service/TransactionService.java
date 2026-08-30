package com.example.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.dto.MonthlyReportResponse;
import com.example.dto.TransactionRequest;
import com.example.dto.TransactionResponse;
import com.example.entity.Transaction;
import com.example.entity.TransactionType;
import com.example.entity.User;
import com.example.exception.ResourceNotFoundException;
import com.example.repo.TransactionRepository;
import com.example.repo.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class TransactionService {
	
	private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private User currentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public TransactionResponse create(String email, TransactionRequest request) {
        User user = currentUser(email);

        Transaction transaction = Transaction.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .date(request.getDate())
                .type(request.getType())
                .user(user)
                .build();

        return TransactionResponse.fromEntity(transactionRepository.save(transaction));
    }

    public TransactionResponse update(String email, Long id, TransactionRequest request) {
        Transaction transaction = getOwnedTransaction(email, id);

        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());

        return TransactionResponse.fromEntity(transactionRepository.save(transaction));
    }

    public void delete(String email, Long id) {
        Transaction transaction = getOwnedTransaction(email, id);
        transactionRepository.delete(transaction);
    }

    public List<TransactionResponse> getAll(String email) {
        User user = currentUser(email);
        return transactionRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<TransactionResponse> search(String email, TransactionType type, String category,
                                             LocalDate startDate, LocalDate endDate, String keyword,
                                             int page, int size) {
        User user = currentUser(email);
        Page<Transaction> results = transactionRepository.search(
                user.getId(), type, category, startDate, endDate, keyword,
                PageRequest.of(page, size));

        return results.map(TransactionResponse::fromEntity);
    }

    public MonthlyReportResponse monthlyReport(String email, int year, int month) {
        User user = currentUser(email);
        List<Transaction> transactions = transactionRepository.findByUserAndMonth(user.getId(), year, month);

        double totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount).sum();

        double totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount).sum();

        double savings = totalIncome - totalExpense;
        double savingsPercentage = totalIncome == 0 ? 0 : (savings / totalIncome) * 100;

        Map<String, Double> expenseByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));

        return new MonthlyReportResponse(year, month, totalIncome, totalExpense,
                savings, savingsPercentage, expenseByCategory);
    }

    private Transaction getOwnedTransaction(String email, Long id) {
        User user = currentUser(email);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            // Don't leak existence of other users' data - same message as "not found"
            throw new ResourceNotFoundException("Transaction not found");
        }
        return transaction;
    }

}
