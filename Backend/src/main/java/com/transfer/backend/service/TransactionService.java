package com.transfer.backend.service;

import com.transfer.backend.dto.TransactionDTO;
import com.transfer.backend.dto.TransferRequestDTO;
import com.transfer.backend.dto.TransferResponseDTO;
import com.transfer.backend.entity.AccountEntity;
import com.transfer.backend.entity.UserEntity;
import com.transfer.backend.enums.TransactionStatus;
import com.transfer.backend.exception.custom.AccountNotFoundException;
import com.transfer.backend.exception.custom.InsufficientFundsException;
import com.transfer.backend.exception.custom.InvalidRecipientException;
import com.transfer.backend.exception.custom.SenderNotFoundException;
import com.transfer.backend.repository.AccountRepository;
import com.transfer.backend.repository.TransactionRepository;
import com.transfer.backend.entity.TransactionEntity; // Correct import for Transaction entity
import com.transfer.backend.repository.UserRepository;
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
