// Backend/src/main/java/com/transfer/backendbankmasr/entity/FavoriteRecipient.java
package com.transfer.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class FavoriteRecipientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String recipientAccountNumber;
}