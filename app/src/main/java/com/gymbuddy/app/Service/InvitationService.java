package com.gymbuddy.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.InvitationRepository;
import com.gymbuddy.app.SocialDomain.Invitation;
import com.gymbuddy.app.SocialDomain.Notification;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private NotificationService notificationService;


    public void sendInvitation(Account sender, Account receiver, WorkoutSession workoutSession) {
        if (workoutSession == null) {
            throw new IllegalArgumentException("Workout session cannot be null");
        }

        Optional<Invitation> existingInvitation = invitationRepository.findBySenderAndReceiverAndWorkoutSession(
            sender, receiver, workoutSession
        );

        if (existingInvitation.isPresent()) {
            throw new RuntimeException("This friend has already been invited to this workout");
        }

        Invitation invite = new Invitation(sender, receiver, workoutSession);
        invitationRepository.save(invite);

        if (notificationService != null) {
            notificationService.notify(
                receiver,
                sender.getUsername() + " invited you to a workout",
                Notification.Type.INVITATION
            );
        }
    }

    public void acceptInvitation(Long inviteId) {
        Invitation invite = invitationRepository.findById(inviteId)
            .orElseThrow(() -> new RuntimeException("Invitation not found"));

        invite.accept();
        invitationRepository.save(invite);

        if (notificationService != null) {
            notificationService.notify(
                invite.getSender(),
                invite.getReceiver().getUsername() + " accepted your invite",
                Notification.Type.INVITE_ACCEPTED
            );
        }
    }

    public void declineInvitation(Long inviteId) {
        Invitation invite = invitationRepository.findById(inviteId)
            .orElseThrow(() -> new RuntimeException("Invitation not found"));

        invite.decline();
        invitationRepository.save(invite);
    }

    public void sendWorkoutInvitation(Account sender, Account receiver) {
        Invitation invitation = new Invitation(sender, receiver);
        invitationRepository.save(invitation);
    }

    public List<Invitation> getPendingInvitationsFor(Account account) {
        return invitationRepository.findByReceiverAndStatus(account, Invitation.Status.PENDING);
    }

    public List<Invitation> getInvitationsForWorkout(WorkoutSession workoutSession, Invitation.Status status) {
        if (workoutSession == null) {
            return List.of();
        }
        if (status == null) {
            return invitationRepository.findByWorkoutSession(workoutSession);
        }
        return invitationRepository.findByWorkoutSessionAndStatus(workoutSession, status);
    }

    public Invitation getInvitationById(Long invitationId) {
        return invitationRepository.findById(invitationId).orElse(null);
    }
}
