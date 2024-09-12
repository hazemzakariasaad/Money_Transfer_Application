package com.transfer.backend.entity;


import com.transfer.backend.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Transaction")
@Builder
@Data

public class TransactionEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "sender_id")
private UserEntity sender;

@ManyToOne
@JoinColumn(name = "recipient_id")
private UserEntity receipient;

private double amount;

@Column(name = "transaction_date")

private LocalDateTime transactionDate;

@Enumerated(EnumType.STRING)
private TransactionStatus status;

}
