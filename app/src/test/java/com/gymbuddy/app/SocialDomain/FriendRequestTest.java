package com.gymbuddy.app.SocialDomain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.gymbuddy.app.AccountDomain.Account;

public class FriendRequestTest {

    @Test
    void constructorShouldCreatePendingFriendRequest() {
        Account sender = new Account("sender@test.com", "senderUser", "password123");
        Account receiver = new Account("receiver@test.com", "receiverUser", "password123");

        FriendRequest request = new FriendRequest(sender, receiver);

        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(FriendRequest.Status.PENDING, request.getStatus());
    }

    @Test
    void acceptFriendRequestShouldSetStatusToAccepted() {
        Account sender = new Account("sender@test.com", "senderUser", "password123");
        Account receiver = new Account("receiver@test.com", "receiverUser", "password123");

        FriendRequest request = new FriendRequest(sender, receiver);

        request.acceptFriendRequest();

        assertEquals(FriendRequest.Status.ACCEPTED, request.getStatus());
    }

    @Test
    void declineFriendRequestShouldSetStatusToDeclined() {
        Account sender = new Account("sender@test.com", "senderUser", "password123");
        Account receiver = new Account("receiver@test.com", "receiverUser", "password123");

        FriendRequest request = new FriendRequest(sender, receiver);

        request.declineFriendRequest();

        assertEquals(FriendRequest.Status.DECLINED, request.getStatus());
    }

    @Test
    void createPendingRequestShouldSetSenderReceiverAndStatus() {
        Account sender = new Account("sender@test.com", "senderUser", "password123");
        Account receiver = new Account("receiver@test.com", "receiverUser", "password123");

        FriendRequest request = new FriendRequest();

        request.createPendingRequest(sender, receiver);

        assertEquals(sender, request.getSender());
        assertEquals(receiver, request.getReceiver());
        assertEquals(FriendRequest.Status.PENDING, request.getStatus());
        assertTrue(receiver.getIncomingRequests().contains(request));
        assertTrue(sender.getSentRequests().contains(request));
    }

    @Test
    void removePendingRequestShouldRemoveRequestFromSenderAndReceiverLists() {
        Account sender = new Account("sender@test.com", "senderUser", "password123");
        Account receiver = new Account("receiver@test.com", "receiverUser", "password123");

        FriendRequest request = new FriendRequest();
        request.createPendingRequest(sender, receiver);

        request.removePendingRequest();

        assertFalse(receiver.getIncomingRequests().contains(request));
        assertFalse(sender.getSentRequests().contains(request));
    }
}