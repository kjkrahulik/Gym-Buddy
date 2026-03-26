package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.SocialDomain.FriendRequest;
import com.gymbuddy.app.AccountDomain.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiver(Account receiver);

    List<FriendRequest> findBySender(Account sender);

    Optional<FriendRequest> findBySenderAndReceiver(Account sender, Account receiver);

    List<FriendRequest> findByReceiverAndStatus(Account receiver, FriendRequest.Status status);
}