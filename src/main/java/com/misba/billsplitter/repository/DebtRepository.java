package com.misba.billsplitter.repository;

import com.misba.billsplitter.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByDebtorIdAndSettledFalse(Long debtorId);
    List<Debt> findByCreditorIdAndSettledFalse(Long creditorId);
    List<Debt> findByGroupIdAndSettledFalse(Long groupId);
}