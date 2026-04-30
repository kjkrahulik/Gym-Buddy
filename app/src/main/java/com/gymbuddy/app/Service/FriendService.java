package com.gymbuddy.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.AccountRepository;
import com.gymbuddy.app.Repositories.FriendRequestRepository;
//import com.gymbuddy.app.Repositories.FriendshipRepository;
import com.gymbuddy.app.SocialDomain.FriendRequest;
//import com.gymbuddy.app.SocialDomain.Notification;
import com.gymbuddy.app.SocialDomain.NotificationFactory;

@Service
public class FriendService {
    
   // @Autowired
    //private FriendshipRepository friendshipRepo;

    @Autowired
    private FriendRequestRepository requestRepo;

   // @Autowired
    //private NotificationService notificationService;

    @Autowired 
    private AccountRepository accountRepo;

    public void sendFriendRequest(Account originator, Account targetAccount) {
        FriendRequest request = new FriendRequest(originator, targetAccount);
        requestRepo.save(request);

        NotificationFactory.createFriendRequestNotification(targetAccount, originator);
    }

    public void acceptRequest(Long requestId) {
        FriendRequest request = requestRepo.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Friend Request not found"));

        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new RuntimeException("Request already handled");
        }

        // Accept request
        request.acceptFriendRequest();
        requestRepo.save(request);

        // Add friends BOTH WAYS
        Account sender = request.getSender();
        Account receiver = request.getReceiver();

        sender.addFriend(receiver);
        accountRepo.save(sender);
        accountRepo.save(receiver);

        // Notify sender
        NotificationFactory.createFriendAcceptedNotification(sender, receiver);
    }
    
    public void removeFriendRequest(Long requestId) {
        FriendRequest request = requestRepo.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Friend Request not found"));

        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new RuntimeException("Request already handled");
        }

        // Decline request
        request.declineFriendRequest();
        requestRepo.save(request);

        // Notify sender
        //NotificationFactory.createFriendDeclinedNotification(request.getSender(), request.getReceiver());
    
}

    public void removeFriend(Account account, Account friend) {
        if (account.getFriends().contains(friend)) {
            account.getFriends().remove(friend);
            friend.getFriends().remove(account); // ensure bidirectional
            accountRepo.save(account);
            accountRepo.save(friend);
        }
        else {
            throw new RuntimeException("Not friends");
        }
    }
}



