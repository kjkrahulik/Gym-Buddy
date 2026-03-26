package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.AccountDomain.AccountTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTestRepository extends JpaRepository<AccountTest, Long> {
    Optional<AccountTest> findByUsername(String username);
}