package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.SocialDomain.Invitation;
import com.gymbuddy.app.AccountDomain.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByReceiver(Account receiver);

    List<Invitation> findBySender(Account sender);

    List<Invitation> findByReceiverAndStatus(Account receiver, Invitation.Status status);
}
