package com.transfer.backendbankmasr.controller;

import com.transfer.backendbankmasr.dto.TransactionHistoryResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionHistoryController {

    @GetMapping
    public ResponseEntity<List<TransactionHistoryResp>> getTransactionHistory(@RequestParam Long userId) {
        // Dummy transaction history data
        TransactionHistoryResp dummyTransaction1 = new TransactionHistoryResp(1L, userId, "Transfer to User 2", 100.00, "2024-09-01");
        TransactionHistoryResp dummyTransaction2 = new TransactionHistoryResp(2L, userId, "Received from User 3", 200.00, "2024-09-02");

        List<TransactionHistoryResp> transactions = Arrays.asList(dummyTransaction1, dummyTransaction2);
        return ResponseEntity.ok(transactions);
    }
}
