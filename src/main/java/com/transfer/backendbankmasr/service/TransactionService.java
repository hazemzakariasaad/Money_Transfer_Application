package com.transfer.backendbankmasr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.backendbankmasr.dto.TransactionDTO;
import com.transfer.backendbankmasr.dto.TransferRequestDTO;
import com.transfer.backendbankmasr.dto.TransferResponseDTO;
import com.transfer.backendbankmasr.dto.UserDTO;
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
import com.transfer.backendbankmasr.service.utilities.Utility;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable; // Correct import for Pageable

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.transfer.backendbankmasr.entity.UserEntity.unproxy;


@Service
public class TransactionService {
@Autowired
private TransactionRepository transactionRepository;

@Autowired
private UserRepository userRepository;
@Autowired
private AccountRepository accountRepository;
@Autowired
private RedisTemplate<String, String> redisTemplate;

@Autowired
    private Utility utils;
    @Autowired
    private ObjectMapper objectMapper ;

public Page<TransactionDTO> getTransactions(Long userId,Pageable pageable) {
    Page<TransactionDTO> transactions=  transactionRepository.findBySenderUserId(userId, pageable).map(this::convertToDTO);
    return transactions;
}


    @Transactional
    public TransferResponseDTO transferMoney(TransferRequestDTO request) throws JsonProcessingException {
        UserEntity sender = userRepository.findById(request.getSenderId())
                .map(user -> unproxy(user))  // Unproxy user
                .orElseThrow(() -> new SenderNotFoundException("Sender not found"));

        AccountEntity senderAccount = sender.getAccounts().stream().findFirst()
                .orElseThrow(() -> new AccountNotFoundException("No account found for sender"));

        AccountEntity recipientAccount = accountRepository.findByAccountNumber(request.getRecipientAccountNumber());
        if (recipientAccount == null) {
            throw new AccountNotFoundException("Recipient account not found");
        }

        // Ensure recipient is unproxied
        UserEntity recipient = unproxy(recipientAccount.getUser());
        if (!recipient.getUsername().equalsIgnoreCase(request.getRecipientName())) {
            throw new InvalidRecipientException("Recipient name does not match account details.");
        }

        // Balance checks and transaction processing
        if (senderAccount.getAccountBalance() < request.getAmount()) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        senderAccount.setAccountBalance(senderAccount.getAccountBalance() - request.getAmount());
        recipientAccount.setAccountBalance(recipientAccount.getAccountBalance() + request.getAmount());

        TransactionEntity transaction = TransactionEntity.builder()
                .sender(sender)
                .receipient(recipient)
                .amount(request.getAmount())
                .transactionDate(LocalDateTime.now())
                .status(TransactionStatus.APPROVED)
                .build();
        transactionRepository.save(transaction);
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        // Update the cache
        updateRedisCache(sender);
        updateRedisCache(recipient);

        return new TransferResponseDTO("Transfer successful", true);
    }

    private void updateRedisCache(UserEntity user) throws JsonProcessingException {
        String redisKey = "userid:" + user.getUserId();
        UserEntity userDTO = user;
        String userData = objectMapper.writeValueAsString(userDTO);
        redisTemplate.opsForValue().set(redisKey, userData, 30, TimeUnit.MINUTES);
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
