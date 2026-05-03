package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.SocialDomain.Invitation;
import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findByReceiver(Account receiver);

    List<Invitation> findBySender(Account sender);

    List<Invitation> findByReceiverAndStatus(Account receiver, Invitation.Status status);

    List<Invitation> findByWorkoutSessionAndStatus(WorkoutSession workoutSession, Invitation.Status status);

    List<Invitation> findByWorkoutSession(WorkoutSession workoutSession);

    Optional<Invitation> findBySenderAndReceiverAndWorkoutSession(Account sender, Account receiver, WorkoutSession workoutSession);
}
