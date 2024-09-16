package com.transfer.backend.repository;

import com.transfer.backend.entity.FavoriteRecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRecipientRepository extends JpaRepository<FavoriteRecipientEntity, Long> {

    List<FavoriteRecipientEntity> findByUser_UserId(Long userId);

    Optional<FavoriteRecipientEntity> findByUser_UserIdAndRecipientAccountNumber(Long userId, String recipientAccountNumber);
}
