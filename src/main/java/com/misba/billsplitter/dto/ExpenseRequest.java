package com.misba.billsplitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ExpenseRequest {
    @NotBlank
    private String description;
    @NotNull
    private Double amount;
    @NotNull
    private Long groupId;
    private List<Long> participantIds;
}