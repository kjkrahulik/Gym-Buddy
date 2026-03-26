package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.SocialDomain.Friendship;
import com.gymbuddy.app.AccountDomain.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUser1OrUser2(Account user1, Account user2);

    boolean existsByUser1AndUser2(Account user1, Account user2);
}
