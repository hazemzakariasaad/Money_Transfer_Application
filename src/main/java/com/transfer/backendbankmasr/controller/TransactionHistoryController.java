package com.transfer.backendbankmasr.controller;

import com.transfer.backendbankmasr.dto.TransactionDTO;
import com.transfer.backendbankmasr.service.TransactionService;
import com.transfer.backendbankmasr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable; // Correct import for Pageable


@RestController

@RequestMapping("/transactions")
public class TransactionHistoryController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
//http://localhost:8080/transactions?page=0&size=10&sort=date,desc
@CrossOrigin
@GetMapping("")
    public Page<TransactionDTO>getTransactionHistory(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Long userId = userService.getUserIdByUsername(username);
        Page<TransactionDTO> transactions = transactionService.getTransactions(userId, pageable);
            return transactions;
    }
    @CrossOrigin
    @GetMapping("/{id}")
    public Page<TransactionDTO >getTransactionHistory(@PathVariable Long id,Pageable pageable) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
//        Long userId = userService.getUserIdByUsername(username);
        Page<TransactionDTO> transactions = transactionService.getTransactions(id, pageable);
        return transactions;
    }
}
