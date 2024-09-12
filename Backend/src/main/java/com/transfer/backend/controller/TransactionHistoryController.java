package com.transfer.backend.controller;

import com.transfer.backend.dto.TransactionDTO;
import com.transfer.backend.service.TransactionService;
import com.transfer.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable; // Correct import for Pageable


@RestController
@CrossOrigin
@RequestMapping("/transactions")
public class TransactionHistoryController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
//http://localhost:8080/transactions?page=0&size=10&sort=date,desc
@GetMapping("")
    public ResponseEntity<Page<TransactionDTO>>getTransactionHistory(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = userService.getUserIdByUsername(username);
        Page<TransactionDTO> transactions = transactionService.getTransactions(userId, pageable);
            return ResponseEntity.ok(transactions);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Page<TransactionDTO>>getTransactionHistory(@PathVariable Long id,Pageable pageable) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
//        Long userId = userService.getUserIdByUsername(username);
        Page<TransactionDTO> transactions = transactionService.getTransactions(id, pageable);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(transactions);
    }
}
