package com.misba.billsplitter.service;

import com.misba.billsplitter.model.Debt;
import com.misba.billsplitter.model.User;
import com.misba.billsplitter.repository.DebtRepository;
import com.misba.billsplitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, Double> getSimplifiedDebts(Long groupId) {
        String cacheKey = "simplified_debts:group:" + groupId;
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return (Map<String, Double>) cached;
        }

        List<Debt> debts = debtRepository.findByGroupIdAndSettledFalse(groupId);
        Map<Long, Double> netBalance = new HashMap<>();

        for (Debt debt : debts) {
            Long debtorId = debt.getDebtor().getId();
            Long creditorId = debt.getCreditor().getId();
            double amount = debt.getAmount();
            netBalance.merge(debtorId, -amount, Double::sum);
            netBalance.merge(creditorId, amount, Double::sum);
        }

        List<Map.Entry<Long, Double>> creditors = new ArrayList<>();
        List<Map.Entry<Long, Double>> debtors = new ArrayList<>();

        for (Map.Entry<Long, Double> entry : netBalance.entrySet()) {
            if (entry.getValue() > 0) creditors.add(entry);
            else if (entry.getValue() < 0) debtors.add(entry);
        }

        Map<String, Double> result = new HashMap<>();
        int i = 0, j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            Map.Entry<Long, Double> debtor = debtors.get(i);
            Map.Entry<Long, Double> creditor = creditors.get(j);

            double debtorOwes = Math.abs(debtor.getValue());
            double creditorNeedsMore = creditor.getValue();
            double settleAmount = Math.min(debtorOwes, creditorNeedsMore);

            String debtorName = userRepository.findById(debtor.getKey())
                    .map(User::getName).orElse("Unknown");
            String creditorName = userRepository.findById(creditor.getKey())
                    .map(User::getName).orElse("Unknown");

            result.put(debtorName + " → " + creditorName,
                    Math.round(settleAmount * 100.0) / 100.0);

            debtor.setValue(debtor.getValue() + settleAmount);
            creditor.setValue(creditor.getValue() - settleAmount);

            if (Math.abs(debtor.getValue()) < 0.01) i++;
            if (Math.abs(creditor.getValue()) < 0.01) j++;
        }

        redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.MINUTES);
        return result;
    }

    public String settleDebt(Long debtId, Long groupId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        debt.setSettled(true);
        debtRepository.save(debt);
        redisTemplate.delete("simplified_debts:group:" + groupId);
        return "Debt settled successfully!";
    }
}