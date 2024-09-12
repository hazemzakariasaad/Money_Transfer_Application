package com.transfer.backendbankmasr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transfer.backendbankmasr.dto.TransferRequestDTO;
import com.transfer.backendbankmasr.dto.TransferResponseDTO;
import com.transfer.backendbankmasr.service.TransactionService;
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
    public TransferResponseDTO transferMoney(@RequestBody TransferRequestDTO req) throws JsonProcessingException {
        return transService.transferMoney(req);
    }
}
