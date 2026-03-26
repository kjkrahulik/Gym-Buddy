package com.gymbuddy.app.SocialDomain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.FriendRequestRepository;
import com.gymbuddy.app.Repositories.FriendshipRepository;

public class FriendshipService {

    private FriendshipRepository friendshipRepo;
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
