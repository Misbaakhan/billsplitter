package com.misba.billsplitter.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExpenseResponse {
    private Long id;
    private String description;
    private Double amount;
    private String paidBy;
    private String groupName;
    private List<String> participants;
    private Double splitAmount;
}