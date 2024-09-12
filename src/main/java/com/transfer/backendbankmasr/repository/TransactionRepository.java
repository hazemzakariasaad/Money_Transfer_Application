package com.transfer.backendbankmasr.repository;

import com.transfer.backendbankmasr.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable; // Correct import for Pageable
import org.springframework.stereotype.Repository;

@Repository

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Page<TransactionEntity> findBySenderUserId(Long userId, Pageable pageable);
}
