package com.transfer.backendbankmasr.controller;

import com.transfer.backendbankmasr.dto.TransactionDTO;
import com.transfer.backendbankmasr.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable; // Correct import for Pageable


@RestController
@CrossOrigin
@RequestMapping("/transactions")
public class TransactionHistoryController {
    @Autowired
    private TransactionService transactionService;
//http://localhost:8080/transactions?userId=1&page=0&size=10&sort=date,desc
    @GetMapping("")
    public ResponseEntity<Page<TransactionDTO>> getTransactionHistory(@RequestParam Long userId,Pageable pageable) {
        Page<TransactionDTO> transactions = transactionService.getTransactions(userId, pageable);
            return ResponseEntity.ok(transactions);
    }
}
