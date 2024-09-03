package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.AccountDTO;
import com.transfer.backendbankmasr.dto.CreateAccountDTO;
import com.transfer.backendbankmasr.dto.UpdateAccountDTO;
import com.transfer.backendbankmasr.entity.Account;
import com.transfer.backendbankmasr.entity.User;
import com.transfer.backendbankmasr.exception.custom.ResourceNotFoundException;
import com.transfer.backendbankmasr.repository.AccountRepository;
import com.transfer.backendbankmasr.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = this.userRepository.findById(accountDTO.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + accountDTO.getUserId() + " not found")
        );

        Account account = Account.builder()
                .accountName(accountDTO.getAccountName())
                .accountBalance(0.0)
                .accountType(accountDTO.getAccountType())
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .accountName(accountDTO.getAccountName())
                .currency(accountDTO.getCurrency())
                .user(user).build();
        Account savedAccount = this.accountRepository.save(account);

        return savedAccount.toDTO();
    }


    @Transactional
    @Override
    public AccountDTO getAccountById(Long accountId) throws ResourceNotFoundException {
        Account account = this.accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account with id " + accountId + " not found")
        );
        return account.toDTO();
    }

    @Override
    public AccountDTO updateAccount(Long accountId, UpdateAccountDTO accountDTO) {
        return null;
    }

    @Override
    public void deleteAccount(Long accountId) {

    }
}
