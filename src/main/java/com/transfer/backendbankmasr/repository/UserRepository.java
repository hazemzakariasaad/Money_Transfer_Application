package com.transfer.backendbankmasr.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.transfer.backendbankmasr.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /// interface inherits several methods such as save(), findAll(), findById(), delete()
    boolean existsByEmail(String email);
    Optional<UserEntity> findUserByEmail(String email);
    boolean existsByUsername(String username);  // Changed from existsByName
    UserEntity findByUsername(String username);
}
