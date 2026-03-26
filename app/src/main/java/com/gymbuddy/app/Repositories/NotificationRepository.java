package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.SocialDomain.Notification;
import com.gymbuddy.app.AccountDomain.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient(Account recipient);

    List<Notification> findByRecipientAndReadFalse(Account recipient);
}
