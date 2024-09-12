package com.transfer.backendbankmasr.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.transfer.backendbankmasr.enums.TransactionStatus;
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

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "sender_id")
@JsonIgnore
private UserEntity sender;

@ManyToOne(fetch = FetchType.EAGER)
@JsonIgnore
@JoinColumn(name = "recipient_id")
private UserEntity receipient;

private double amount;

@Column(name = "transaction_date")

private LocalDateTime transactionDate;

@Enumerated(EnumType.STRING)
private TransactionStatus status;

}
