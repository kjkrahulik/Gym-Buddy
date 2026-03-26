package com.gymbuddy.app.SocialDomain;

import com.gymbuddy.app.AccountDomain.Account;

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
        FriendRequest request = requestRepo.findById(requestId);

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
