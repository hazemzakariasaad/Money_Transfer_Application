// Backend/src/main/java/com/transfer/backendbankmasr/repository/FavoriteRecipientRepository.java
package com.transfer.backendbankmasr.repository;

import com.transfer.backendbankmasr.entity.FavoriteRecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRecipientRepository extends JpaRepository<FavoriteRecipientEntity, Long> {
    List<FavoriteRecipientEntity> findByUser_UserId(Long userId);  // Corrected method name
}
