package com.transfer.backendbankmasr.controller;

import com.transfer.backendbankmasr.dto.MoneyTransferReq;
import com.transfer.backendbankmasr.dto.MoneyTransferResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class MoneyTransferController {

    @PostMapping
    public ResponseEntity<MoneyTransferResp> transferMoney(@RequestBody MoneyTransferReq req) {
        // Dummy transfer response
        MoneyTransferResp dummyResponse = new MoneyTransferResp(req.getSenderId(), req.getRecipientId(), req.getAmount(), "Transfer successful");
        return ResponseEntity.ok(dummyResponse);
    }
}
