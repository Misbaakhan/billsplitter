package com.misba.billsplitter.repository;

import com.misba.billsplitter.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroupId(Long groupId);
    List<Expense> findByPaidById(Long userId);
}