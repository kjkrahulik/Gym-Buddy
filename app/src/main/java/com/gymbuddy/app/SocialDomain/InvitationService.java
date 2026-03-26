package com.gymbuddy.app.SocialDomain;

import java.util.Optional;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.InvitationRepository;

public class InvitationService {

    private InvitationRepository invitationRepo;
    private NotificationService notificationService;

    public void sendInvitation(Account sender, Account receiver, Long workoutId) {
        Invitation invite = new Invitation(sender, receiver, workoutId);
        invitationRepo.save(invite);

        notificationService.notify(
            receiver,
            sender.getUsername() + " invited you to a workout",
            Notification.Type.INVITATION
        );
    }

    public void acceptInvitation(Long inviteId) {
        Invitation invite =  invitationRepo.findById(inviteId).orElseThrow(()->new RuntimeException("Invitation not found"));

        invite.accept();
        invitationRepo.save(invite);
        notificationService.notify(invite.getSender(),invite.getReceiver().getUsername() + " accepted your invite",Notification.Type.INVITE_ACCEPTED);
    }

}
