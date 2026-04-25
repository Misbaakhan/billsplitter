package com.misba.billsplitter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class GroupRequest {
    @NotBlank
    private String name;
    private List<Long> memberIds;
}