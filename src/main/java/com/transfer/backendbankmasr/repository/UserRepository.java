package com.transfer.backendbankmasr.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.transfer.backendbankmasr.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /// interface inherits several methods such as save(), findAll(), findById(), delete()
    boolean existsByEmail(String email);
    boolean existsByName(String email);
}
