package com.transfer.backend.controller;


import com.transfer.backend.dto.AccountDTO;
import com.transfer.backend.dto.CreateAccountDTO;
import com.transfer.backend.dto.UpdateAccountDTO;
import com.transfer.backend.exception.custom.ResourceNotFoundException;
import com.transfer.backend.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Account Controller", description = "Account controller")
public class AccountController {
private final AccountService accountService;

@GetMapping("/{accountId}")
    public AccountDTO getAccountById(@PathVariable long accountId) {
   return this.accountService.getAccountById(accountId);
}
@PostMapping("")
    public AccountDTO createAccount(@RequestBody @Valid CreateAccountDTO accountDTO) {
    return this.accountService.createAccount(accountDTO);
}
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long accountId, @RequestBody UpdateAccountDTO accountDTO) throws ResourceNotFoundException {
        AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) throws ResourceNotFoundException {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
