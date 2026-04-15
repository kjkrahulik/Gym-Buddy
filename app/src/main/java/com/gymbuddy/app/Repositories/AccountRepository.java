package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.AccountDomain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);


    boolean existsByUsername(String username); 
    boolean existsByEmail(String email);
}