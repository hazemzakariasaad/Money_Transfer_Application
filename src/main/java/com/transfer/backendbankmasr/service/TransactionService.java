package com.transfer.backendbankmasr.service;

import com.transfer.backendbankmasr.dto.TransactionDTO;
import com.transfer.backendbankmasr.dto.TransferRequestDTO;
import com.transfer.backendbankmasr.dto.TransferResponseDTO;
import com.transfer.backendbankmasr.entity.AccountEntity;
import com.transfer.backendbankmasr.entity.UserEntity;
import com.transfer.backendbankmasr.enums.TransactionStatus;
import com.transfer.backendbankmasr.exception.custom.AccountNotFoundException;
import com.transfer.backendbankmasr.exception.custom.InsufficientFundsException;
import com.transfer.backendbankmasr.exception.custom.InvalidRecipientException;
import com.transfer.backendbankmasr.exception.custom.SenderNotFoundException;
import com.transfer.backendbankmasr.repository.AccountRepository;
import com.transfer.backendbankmasr.repository.TransactionRepository;
import com.transfer.backendbankmasr.entity.TransactionEntity; // Correct import for Transaction entity
import com.transfer.backendbankmasr.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable; // Correct import for Pageable

import java.time.LocalDateTime;


@Service
public class TransactionService {
@Autowired
private TransactionRepository transactionRepository;

@Autowired
private UserRepository userRepository;
@Autowired
private AccountRepository accountRepository;


public Page<TransactionDTO> getTransactions(Long userId,Pageable pageable) {
    Page<TransactionDTO> transactions=  transactionRepository.findBySenderUserId(userId, pageable).map(this::convertToDTO);
    return transactions;
}


@Transactional
public TransferResponseDTO transferMoney(TransferRequestDTO request) {
        UserEntity sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new SenderNotFoundException("Sender not found"));

        AccountEntity senderAccount = sender.getAccounts().stream().findFirst()
                .orElseThrow(() -> new AccountNotFoundException("No account found for sender"));

        AccountEntity recipientAccount = accountRepository.findByAccountNumber(request.getRecipientAccountNumber());
        if (recipientAccount == null) {
            throw new AccountNotFoundException("Recipient account not found");
        }
        if (!recipientAccount.getUser().getUsername().equalsIgnoreCase(request.getRecipientName())) {
            throw new InvalidRecipientException("Recipient name does not match account details.");
        }
        if (senderAccount.getAccountBalance() < request.getAmount()) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        // Process the transfer
        senderAccount.setAccountBalance(senderAccount.getAccountBalance() - request.getAmount());
        recipientAccount.setAccountBalance(recipientAccount.getAccountBalance() + request.getAmount());

        TransactionEntity transaction =  TransactionEntity.builder()
                .sender(sender)
                .receipient(recipientAccount.getUser())
                .amount(request.getAmount())
                .transactionDate(LocalDateTime.now())
                .status(TransactionStatus.APPROVED)
                .build();
    transactionRepository.save(transaction);
    accountRepository.save(senderAccount);
    accountRepository.save(recipientAccount);

        return new TransferResponseDTO("Transfer successful", true);
    }


private TransactionDTO convertToDTO(TransactionEntity transaction) {
    return TransactionDTO.builder()
            .transactionId(transaction.getId())
            .AccountNumber(transaction.getSender().getAccounts().stream().findFirst().get().getAccountNumber())
            .amount(transaction.getAmount())
            .recipientName(transaction.getReceipient().getUsername())
            .status(transaction.getStatus().name())
            .transactionDate(transaction.getTransactionDate())
            .build();
}

}
