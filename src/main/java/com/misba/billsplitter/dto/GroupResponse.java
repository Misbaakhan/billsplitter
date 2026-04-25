package com.misba.billsplitter.dto;

import lombok.Data;
import java.util.List;

@Data
public class GroupResponse {
    private Long id;
    private String name;
    private String createdBy;
    private List<String> members;
}