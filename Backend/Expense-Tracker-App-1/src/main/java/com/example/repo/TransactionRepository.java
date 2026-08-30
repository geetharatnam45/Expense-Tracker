package com.example.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.entity.Transaction;
import com.example.entity.TransactionType;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    // Search & filter: any of the params may be null and is simply skipped
    @Query("""
           SELECT t FROM Transaction t
           WHERE t.user.id = :userId
             AND (:type IS NULL OR t.type = :type)
             AND (:category IS NULL OR t.category = :category)
             AND (:startDate IS NULL OR t.date >= :startDate)
             AND (:endDate IS NULL OR t.date <= :endDate)
             AND (:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
           ORDER BY t.date DESC
           """)
    Page<Transaction> search(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("""
           SELECT t FROM Transaction t
           WHERE t.user.id = :userId
             AND YEAR(t.date) = :year
             AND MONTH(t.date) = :month
           """)
    List<Transaction> findByUserAndMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);
}
