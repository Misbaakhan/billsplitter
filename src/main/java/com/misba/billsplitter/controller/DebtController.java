package com.misba.billsplitter.controller;

import com.misba.billsplitter.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;

    @GetMapping("/simplified/{groupId}")
    public ResponseEntity<Map<String, Double>> getSimplifiedDebts(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(debtService.getSimplifiedDebts(groupId));
    }

    @PutMapping("/settle/{debtId}/group/{groupId}")
    public ResponseEntity<String> settleDebt(
            @PathVariable Long debtId,
            @PathVariable Long groupId) {
        return ResponseEntity.ok(debtService.settleDebt(debtId, groupId));
    }
}