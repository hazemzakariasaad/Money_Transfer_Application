package com.transfer.backendbankmasr.entity;
import com.transfer.backendbankmasr.dto.AccountDTO;
import com.transfer.backendbankmasr.enums.AccountCurrency;
import com.transfer.backendbankmasr.enums.AccountStatus;
import com.transfer.backendbankmasr.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Accounts")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private Account parentAccount;
    //many accounts to one parent


    @OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL)
    // relation between current entity and another entity .
    private List<Account> subAccounts;

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
