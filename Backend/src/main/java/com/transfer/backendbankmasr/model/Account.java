package com.transfer.backendbankmasr.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

enum AccountType{
    CREDIT,SAVING;
}
enum AccountStatus {
    ACTIVE, INACTIVE, SUSPENDED
}
@Entity
@Table(name="Accounts")
@Setter
@Getter
public class Account {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;
    private String accountName;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private BigDecimal accountBalance;
    private LocalDateTime AccountCreationDate;
    private LocalDateTime AccountUpdateDate;
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

}
