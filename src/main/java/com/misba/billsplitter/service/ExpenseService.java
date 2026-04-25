package com.misba.billsplitter.service;

import com.misba.billsplitter.dto.ExpenseRequest;
import com.misba.billsplitter.dto.ExpenseResponse;
import com.misba.billsplitter.model.Debt;
import com.misba.billsplitter.model.Expense;
import com.misba.billsplitter.model.Group;
import com.misba.billsplitter.model.User;
import com.misba.billsplitter.repository.DebtRepository;
import com.misba.billsplitter.repository.ExpenseRepository;
import com.misba.billsplitter.repository.GroupRepository;
import com.misba.billsplitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final DebtRepository debtRepository;

    public ExpenseResponse addExpense(ExpenseRequest request, String email) {
        User paidBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<User> participants = new ArrayList<>(
                userRepository.findAllById(request.getParticipantIds()));

        if (!participants.contains(paidBy)) {
            participants.add(paidBy);
        }

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPaidBy(paidBy);
        expense.setGroup(group);
        expense.setParticipants(participants);
        expenseRepository.save(expense);

        double splitAmount = request.getAmount() / participants.size();
        for (User participant : participants) {
            if (!participant.getId().equals(paidBy.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(participant);
                debt.setCreditor(paidBy);
                debt.setGroup(group);
                debt.setAmount(splitAmount);
                debt.setSettled(false);
                debtRepository.save(debt);
            }
        }

        return mapToResponse(expense, splitAmount);
    }

    public List<ExpenseResponse> getGroupExpenses(Long groupId) {
        return expenseRepository.findByGroupId(groupId)
                .stream()
                .map(e -> mapToResponse(e, e.getAmount() / e.getParticipants().size()))
                .collect(Collectors.toList());
    }

    private ExpenseResponse mapToResponse(Expense expense, double splitAmount) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setPaidBy(expense.getPaidBy().getName());
        response.setGroupName(expense.getGroup().getName());
        response.setParticipants(expense.getParticipants()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList()));
        response.setSplitAmount(splitAmount);
        return response;
    }
}