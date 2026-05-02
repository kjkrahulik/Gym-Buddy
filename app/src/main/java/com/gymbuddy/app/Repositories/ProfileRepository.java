package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.AccountDomain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByAccount_Username(String username);
}
