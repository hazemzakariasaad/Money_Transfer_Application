package com.transfer.backend.service;

import com.transfer.backend.dto.AccountDTO;
import com.transfer.backend.dto.CreateAccountDTO;
import com.transfer.backend.dto.UpdateAccountDTO;
import com.transfer.backend.entity.AccountEntity;
import com.transfer.backend.entity.UserEntity;
import com.transfer.backend.exception.custom.ResourceNotFoundException;
import com.transfer.backend.repository.AccountRepository;
import com.transfer.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public AccountDTO createAccount(CreateAccountDTO accountDTO) throws ResourceNotFoundException {
        UserEntity user = this.userRepository.findById(accountDTO.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + accountDTO.getUserId() + " not found")
        );
        //add validation for balance as when create account balance check if this balance in first accont or not
        // then add this
        AccountEntity account = AccountEntity.builder()
                .accountName(accountDTO.getAccountName())
                .accountBalance(0.0)
                .accountType(accountDTO.getAccountType())
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .currency(accountDTO.getCurrency())
                .user(user).build();
        AccountEntity savedAccount = this.accountRepository.save(account);
        return savedAccount.toDTO();
    }

    //get account of current user by userID
    @Transactional
    @Override
    public AccountDTO getAccountById(Long accountId) throws ResourceNotFoundException {
        AccountEntity account = this.accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account with id " + accountId + " not found")
        );
        return account.toDTO();
    }

    // add update or delete account by userId

    @Override
    public AccountDTO updateAccount(Long accountId, UpdateAccountDTO accountDTO) {
        AccountEntity account = this.accountRepository.findById(accountId).orElseThrow(()->
                new ResourceNotFoundException("Account with id " + accountId + " not found"));
        if (accountDTO.getAccountName() != null) {
            account.setAccountName(accountDTO.getAccountName());
        }

        AccountEntity updatedAccount = accountRepository.save(account);
        return updatedAccount.toDTO();
    }

    @Override
    public void deleteAccount(Long accountId) {
        AccountEntity account = this.accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account with id " + accountId + " not found")
        );
    accountRepository.delete(account);
    }

}
