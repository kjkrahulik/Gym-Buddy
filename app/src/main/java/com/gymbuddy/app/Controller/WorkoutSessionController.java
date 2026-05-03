package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.ExerciseRepository;
import com.gymbuddy.app.Repositories.WorkoutSessionRepository;
import com.gymbuddy.app.Repositories.WorkoutTemplateRepository;
import com.gymbuddy.app.Service.AccountService;
import com.gymbuddy.app.Service.InvitationService;
import com.gymbuddy.app.Service.ProfileService;
import com.gymbuddy.app.SocialDomain.Invitation;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.TimedSet;
import com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSessionDTO;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutSessionController {

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<WorkoutSessionDTO>> getUserWorkouts(Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<WorkoutSession> sessions = workoutSessionRepository.findByAccountOrderBySessionDateDesc(account);

            List<WorkoutSessionDTO> dtos = sessions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<Map<String, Object>> updateWorkout(
            @PathVariable Long sessionId,
            @RequestBody SaveWorkoutRequest request,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = workoutSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));

            if (!session.getAccount().equals(account)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            session.setSessionName(request.getSessionName() != null ? request.getSessionName() : session.getSessionName());
            session.setNotes(request.getNotes());
            session.getSets().clear();

            int exerciseOrder = 0;
            if (request.getExercises() != null) {
                for (SaveWorkoutRequest.ExerciseRequest exReq : request.getExercises()) {
                    Exercise exercise = exerciseRepository.findById(exReq.getExerciseID()).orElse(null);
                    if (exercise != null) {
                        session.addExercise(exercise);

                        if (exReq.getSets() != null) {
                            for (int setIdx = 0; setIdx < exReq.getSets().size(); setIdx++) {
                                SaveWorkoutRequest.SetRequest setReq = exReq.getSets().get(setIdx);

                                if ("WEIGHTED".equals(setReq.getType())) {
                                    WeightedSet ws = new WeightedSet();
                                    ws.setWeight(setReq.getWeight() != null ? setReq.getWeight() : 0);
                                    ws.setReps(setReq.getReps() != null ? setReq.getReps() : 0);
                                    ws.setExerciseOrder(exerciseOrder);
                                    ws.setOrderIndex(setIdx);
                                    session.getSets().add(ws);
                                } else if ("TIMED".equals(setReq.getType())) {
                                    TimedSet ts = new TimedSet();
                                    ts.setDurationInSeconds(setReq.getDuration() != null ? setReq.getDuration() : 0);
                                    ts.setExerciseOrder(exerciseOrder);
                                    ts.setOrderIndex(setIdx);
                                    session.getSets().add(ts);
                                }
                            }
                        }
                        exerciseOrder++;
                    }
                }
            }

            WorkoutSession updated = workoutSessionRepository.save(session);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionID", updated.getListID());
            response.put("message", "Workout updated successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to update workout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveWorkout(
            @RequestBody SaveWorkoutRequest request,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = new WorkoutSession();
            String sessionName = request.getSessionName() != null ? request.getSessionName() : "Untitled Workout";
            session.setSessionName(sessionName);
            session.setListName(sessionName);
            session.setNotes(request.getNotes());
            session.setSessionDate(LocalDateTime.now());
            session.setAccount(account);

            // Add exercises and sets
            int exerciseOrder = 0;
            if (request.getExercises() != null) {
                for (SaveWorkoutRequest.ExerciseRequest exReq : request.getExercises()) {
                    // Fetch exercise from database
                    Exercise exercise = exerciseRepository.findById(exReq.getExerciseID()).orElse(null);
                    if (exercise != null) {
                        session.addExercise(exercise);

                        // Add sets for this exercise
                        if (exReq.getSets() != null) {
                            for (int setIdx = 0; setIdx < exReq.getSets().size(); setIdx++) {
                                SaveWorkoutRequest.SetRequest setReq = exReq.getSets().get(setIdx);

                                if ("WEIGHTED".equals(setReq.getType())) {
                                    WeightedSet ws = new WeightedSet();
                                    ws.setWeight(setReq.getWeight() != null ? setReq.getWeight() : 0);
                                    ws.setReps(setReq.getReps() != null ? setReq.getReps() : 0);
                                    ws.setExerciseOrder(exerciseOrder);
                                    ws.setOrderIndex(setIdx);
                                    session.getSets().add(ws);
                                } else if ("TIMED".equals(setReq.getType())) {
                                    TimedSet ts = new TimedSet();
                                    ts.setDurationInSeconds(setReq.getDuration() != null ? setReq.getDuration() : 0);
                                    ts.setExerciseOrder(exerciseOrder);
                                    ts.setOrderIndex(setIdx);
                                    session.getSets().add(ts);
                                }
                            }
                        }
                        exerciseOrder++;
                    }
                }
            }

            WorkoutSession saved = workoutSessionRepository.save(session);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionID", saved.getListID());
            response.put("message", "Workout saved successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to save workout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/from-template/{templateId}")
    public ResponseEntity<Map<String, Object>> createSessionFromTemplate(
            @PathVariable Long templateId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutTemplate template = workoutTemplateRepository.findById(templateId)
                    .orElseThrow(() -> new IllegalArgumentException("Template not found"));

            WorkoutSession session = new WorkoutSession();
            session.setSessionName(template.getListName());
            session.setListName(template.getListName());
            session.setNotes(template.getNotes());
            session.setSessionDate(LocalDateTime.now());
            session.setAccount(account);

            // Copy exercises from template (no sets)
            template.getExercises().forEach((order, exercise) -> session.addExercise(exercise));

            WorkoutSession saved = workoutSessionRepository.save(session);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionID", saved.getListID());
            response.put("message", "Workout session created from template");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create session from template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{sessionId}/save-as-template")
    public ResponseEntity<Map<String, Object>> saveSessionAsTemplate(
            @PathVariable Long sessionId,
            @RequestBody Map<String, String> request,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = workoutSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));

            if (!session.getAccount().equals(account)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String templateName = request.get("templateName");
            if (templateName == null || templateName.trim().isEmpty()) {
                templateName = generateUniqueTemplateName();
            }

            String notes = request.getOrDefault("notes", "");

            WorkoutTemplate template = new WorkoutTemplate(templateName, notes);
            session.getExercises().forEach((order, exercise) -> template.addExercise(exercise));
            WorkoutTemplate saved = workoutTemplateRepository.save(template);

            Map<String, Object> response = new HashMap<>();
            response.put("templateId", saved.getListID());
            response.put("templateName", saved.getListName());
            response.put("message", "Workout saved as template successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to save workout as template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    private String generateUniqueTemplateName() {
        int counter = 1;
        String templateName;
        while (true) {
            templateName = "Workout Template #" + counter;
            if (workoutTemplateRepository.findByListName(templateName).isEmpty()) {
                return templateName;
            }
            counter++;
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<WorkoutSessionDTO> getWorkout(
            @PathVariable Long sessionId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = workoutSessionRepository.findById(sessionId).orElse(null);

            if (session == null || !session.getAccount().equals(account)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            WorkoutSessionDTO dto = convertToDTO(session);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private WorkoutSessionDTO convertToDTO(WorkoutSession session) {
        WorkoutSessionDTO dto = new WorkoutSessionDTO();
        dto.setSessionID(session.getListID());
        dto.setSessionName(session.getSessionName());
        dto.setSessionDate(session.getSessionDate());
        dto.setNotes(session.getNotes());
        dto.setExerciseCount(session.getNumExercises());

        // Calculate total sets and volume
        int totalSets = session.getSets().size();
        float totalVolume = 0;

        for (com.gymbuddy.app.WorkoutDomain.Exercise.Set set : session.getSets()) {
            if (set instanceof com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet) {
                com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet ws =
                        (com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet) set;
                totalVolume += ws.getWeight() * ws.getReps();
            }
        }

        dto.setTotalSets(totalSets);
        dto.setTotalVolume(totalVolume);

        // Get invited participants usernames
        List<String> participantUsernames = session.getInvitedParticipants().stream()
                .map(Account::getUsername)
                .collect(Collectors.toList());
        dto.setInvitedParticipants(participantUsernames);

        // Build exercises list
        List<WorkoutSessionDTO.ExerciseDTO> exercises = session.getExercises().entrySet()
                .stream()
                .map(entry -> {
                    Exercise exercise = entry.getValue();
                    List<com.gymbuddy.app.WorkoutDomain.Exercise.Set> exerciseSets =
                            session.getSetsByExercise(entry.getKey());
                    return new WorkoutSessionDTO.ExerciseDTO(exercise, exerciseSets);
                })
                .collect(Collectors.toList());

        dto.setExercises(exercises);

        return dto;
    }

    @GetMapping("/{sessionId}/friends")
    public ResponseEntity<?> getFriendsForInvite(
            @PathVariable Long sessionId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<FriendDTO> friends = account.getFriends().stream()
                .map(friend -> {
                    Profile friendProfile = profileService.getProfile(friend.getUsername());
                    String profilePictureBase64 = friendProfile != null ? friendProfile.getProfilePictureBase64() : "";
                    return new FriendDTO(friend.getAccountID(), friend.getUsername(), profilePictureBase64);
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{sessionId}/invite-friend")
    public ResponseEntity<Map<String, Object>> inviteFriendToSession(
            @PathVariable Long sessionId,
            @RequestBody InviteFriendRequest request,
            Principal principal) {
        try {
            String username = principal.getName();
            Account sender = accountService.searchAccount(username);

            if (sender == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession workoutSession = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));

            if (!workoutSession.getAccount().equals(sender)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Account receiver = accountService.getAccountById(request.getFriendId());
            if (receiver == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Friend not found");
                return ResponseEntity.badRequest().body(error);
            }

            invitationService.sendInvitation(sender, receiver, workoutSession);
            workoutSession.addParticipant(receiver);
            workoutSessionRepository.save(workoutSession);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invitation sent successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to send invitation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{sessionId}/invitations")
    public ResponseEntity<List<ParticipantDTO>> getSessionInvitations(
            @PathVariable Long sessionId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession workoutSession = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));

            List<Invitation> invitations = invitationService.getInvitationsForWorkout(workoutSession, null);

            List<ParticipantDTO> participants = invitations.stream()
                .map(invitation -> {
                    Account participant = invitation.getReceiver();
                    Profile participantProfile = profileService.getProfile(participant.getUsername());
                    String profilePictureBase64 = participantProfile != null ? participantProfile.getProfilePictureBase64() : "";
                    return new ParticipantDTO(
                        invitation.getId(),
                        participant.getAccountID(),
                        participant.getUsername(),
                        profilePictureBase64,
                        invitation.getStatus().toString()
                    );
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Map<String, Object>> deleteWorkout(
            @PathVariable Long sessionId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));

            if (!session.getAccount().equals(account)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            workoutSessionRepository.delete(session);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Workout session deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to delete workout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/pending-invitations")
    public ResponseEntity<List<WorkoutInvitationDTO>> getPendingWorkoutInvitations(Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<Invitation> pendingInvitations = invitationService.getPendingInvitationsFor(account)
                .stream()
                .filter(inv -> inv.getWorkoutSession() != null)
                .toList();

            List<WorkoutInvitationDTO> dtos = pendingInvitations.stream()
                .map(invitation -> {
                    Account sender = invitation.getSender();
                    WorkoutSession session = invitation.getWorkoutSession();
                    Profile senderProfile = profileService.getProfile(sender.getUsername());
                    String senderProfilePictureBase64 = senderProfile != null ? senderProfile.getProfilePictureBase64() : "";

                    return new WorkoutInvitationDTO(
                        invitation.getId(),
                        session.getListID(),
                        sender.getAccountID(),
                        sender.getUsername(),
                        senderProfilePictureBase64,
                        session.getSessionName(),
                        session.getSessionDate(),
                        invitation.getStatus().toString(),
                        session.getStatus().toString()
                    );
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{sessionId}/accept-invitation")
    public ResponseEntity<Map<String, Object>> acceptInvitation(
            @PathVariable Long sessionId,
            @RequestBody AcceptInvitationRequest request) {
        try {
            Invitation invitation = invitationService.getInvitationById(request.getInvitationId());
            if (invitation == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Invitation not found");
                return ResponseEntity.badRequest().body(error);
            }

            WorkoutSession session = invitation.getWorkoutSession();
            if (session == null || session.getStatus() != WorkoutSession.Status.LIVE) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "This workout session is no longer active");
                return ResponseEntity.badRequest().body(error);
            }

            invitationService.acceptInvitation(request.getInvitationId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invitation accepted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Request DTOs
    public static class InviteFriendRequest {
        private UUID friendId;

        public UUID getFriendId() {
            return friendId;
        }

        public void setFriendId(UUID friendId) {
            this.friendId = friendId;
        }
    }

    public static class AcceptInvitationRequest {
        private Long invitationId;

        public Long getInvitationId() {
            return invitationId;
        }

        public void setInvitationId(Long invitationId) {
            this.invitationId = invitationId;
        }
    }

    public static class FriendDTO {
        private UUID accountId;
        private String username;
        private String profilePictureBase64;

        public FriendDTO(UUID accountId, String username, String profilePictureBase64) {
            this.accountId = accountId;
            this.username = username;
            this.profilePictureBase64 = profilePictureBase64;
        }

        public UUID getAccountId() {
            return accountId;
        }

        public String getUsername() {
            return username;
        }

        public String getProfilePictureBase64() {
            return profilePictureBase64;
        }
    }

    public static class ParticipantDTO {
        private Long invitationId;
        private UUID accountId;
        private String username;
        private String profilePictureBase64;
        private String invitationStatus;

        public ParticipantDTO(Long invitationId, UUID accountId, String username, String profilePictureBase64, String invitationStatus) {
            this.invitationId = invitationId;
            this.accountId = accountId;
            this.username = username;
            this.profilePictureBase64 = profilePictureBase64;
            this.invitationStatus = invitationStatus;
        }

        public Long getInvitationId() {
            return invitationId;
        }

        public UUID getAccountId() {
            return accountId;
        }

        public String getUsername() {
            return username;
        }

        public String getProfilePictureBase64() {
            return profilePictureBase64;
        }

        public String getInvitationStatus() {
            return invitationStatus;
        }
    }

    public static class WorkoutInvitationDTO {
        private Long invitationId;
        private Long sessionId;
        private UUID senderId;
        private String senderUsername;
        private String senderProfilePictureBase64;
        private String sessionName;
        private LocalDateTime sessionDate;
        private String invitationStatus;
        private String sessionStatus;

        public WorkoutInvitationDTO(Long invitationId, Long sessionId, UUID senderId, String senderUsername,
                                   String senderProfilePictureBase64, String sessionName, LocalDateTime sessionDate, String invitationStatus, String sessionStatus) {
            this.invitationId = invitationId;
            this.sessionId = sessionId;
            this.senderId = senderId;
            this.senderUsername = senderUsername;
            this.senderProfilePictureBase64 = senderProfilePictureBase64;
            this.sessionName = sessionName;
            this.sessionDate = sessionDate;
            this.invitationStatus = invitationStatus;
            this.sessionStatus = sessionStatus;
        }

        public Long getInvitationId() {
            return invitationId;
        }

        public Long getSessionId() {
            return sessionId;
        }

        public UUID getSenderId() {
            return senderId;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public String getSenderProfilePictureBase64() {
            return senderProfilePictureBase64;
        }

        public String getSessionName() {
            return sessionName;
        }

        public LocalDateTime getSessionDate() {
            return sessionDate;
        }

        public String getInvitationStatus() {
            return invitationStatus;
        }

        public String getSessionStatus() {
            return sessionStatus;
        }
    }

    public static class SaveWorkoutRequest {
        private String sessionName;
        private String notes;
        private List<ExerciseRequest> exercises;

        public String getSessionName() {
            return sessionName;
        }

        public void setSessionName(String sessionName) {
            this.sessionName = sessionName;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public List<ExerciseRequest> getExercises() {
            return exercises;
        }

        public void setExercises(List<ExerciseRequest> exercises) {
            this.exercises = exercises;
        }

        public static class ExerciseRequest {
            private Long exerciseID;
            private String exerciseName;
            private String muscleGroup;
            private List<SetRequest> sets;

            public Long getExerciseID() {
                return exerciseID;
            }

            public void setExerciseID(Long exerciseID) {
                this.exerciseID = exerciseID;
            }

            public String getExerciseName() {
                return exerciseName;
            }

            public void setExerciseName(String exerciseName) {
                this.exerciseName = exerciseName;
            }

            public String getMuscleGroup() {
                return muscleGroup;
            }

            public void setMuscleGroup(String muscleGroup) {
                this.muscleGroup = muscleGroup;
            }

            public List<SetRequest> getSets() {
                return sets;
            }

            public void setSets(List<SetRequest> sets) {
                this.sets = sets;
            }
        }

        public static class SetRequest {
            private String type;
            private Float weight;
            private Integer reps;
            private Integer duration;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Float getWeight() {
                return weight;
            }

            public void setWeight(Float weight) {
                this.weight = weight;
            }

            public Integer getReps() {
                return reps;
            }

            public void setReps(Integer reps) {
                this.reps = reps;
            }

            public Integer getDuration() {
                return duration;
            }

            public void setDuration(Integer duration) {
                this.duration = duration;
            }
        }
    }
}
