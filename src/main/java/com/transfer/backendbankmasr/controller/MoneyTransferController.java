package com.transfer.backendbankmasr.controller;

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
    public ResponseEntity<TransferResponseDTO> transferMoney(@RequestBody TransferRequestDTO req) {
        return ResponseEntity.ok(transService.transferMoney(req));
    }
}
