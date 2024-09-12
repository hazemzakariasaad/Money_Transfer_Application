package com.transfer.backend.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.transfer.backend.dto.AccountDTO;
import com.transfer.backend.enums.AccountCurrency;
import com.transfer.backend.enums.AccountStatus;
import com.transfer.backend.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Accounts")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;

    private String accountName;

    @Column(nullable = false, unique = true)
    private String accountNumber;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private double accountBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private AccountEntity parentAccount;
    //many accounts to one parent


    @OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL)
    // relation between current entity and another entity .
    private List<AccountEntity> subAccounts;

 public AccountDTO toDTO() {
  return AccountDTO.builder()
          .id(this.accountId)
          .accountNumber(this.accountNumber)
          .accountType(this.accountType)
          .balance(this.accountBalance)
          .currency(this.currency)
          .accountName(this.accountName)
          .status(this.accountStatus)
          .createdAt(this.createdAt)
          .updatedAt(this.updatedAt)
          .build();
 }
}
