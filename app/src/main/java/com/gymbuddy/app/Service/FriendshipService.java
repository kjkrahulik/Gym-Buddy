package com.gymbuddy.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.FriendRequestRepository;
import com.gymbuddy.app.Repositories.FriendshipRepository;
import com.gymbuddy.app.SocialDomain.FriendRequest;
import com.gymbuddy.app.SocialDomain.Friendship;
import com.gymbuddy.app.SocialDomain.Notification;

@Service
public class FriendshipService {
    
    @Autowired
    private FriendshipRepository friendshipRepo;

    @Autowired
    private FriendRequestRepository requestRepo;

    private NotificationService notificationService;

    public void sendFriendRequest(Account sender, Account receiver) {
        FriendRequest request = new FriendRequest(sender, receiver);
        requestRepo.save(request);

        notificationService.notify(
            receiver,
            sender.getUsername() + " sent you a friend request",
            Notification.Type.FRIEND_REQUEST
        );
    }

    public void acceptRequest(Long requestId) {
        FriendRequest request = requestRepo.findById(requestId).orElseThrow(() -> new RuntimeException("Friend request not found"));;

        request.accept();
        requestRepo.save(request);

        Friendship friendship = new Friendship(
            request.getSender(),
            request.getReceiver()
        );
        friendshipRepo.save(friendship);

        notificationService.notify(
            request.getSender(),
            request.getReceiver().getUsername() + " accepted your request",
            Notification.Type.FRIEND_ACCEPTED
        );
    }
    
}
