package com.transfer.backend.controller;

import com.transfer.backend.dto.TransferRequestDTO;
import com.transfer.backend.dto.TransferResponseDTO;
import com.transfer.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/transfer")
public class MoneyTransferController {

    @Autowired
    private TransactionService transService;
    @PostMapping("")
    public TransferResponseDTO transferMoney(@RequestBody TransferRequestDTO req) {
        return transService.transferMoney(req);
    }
}
