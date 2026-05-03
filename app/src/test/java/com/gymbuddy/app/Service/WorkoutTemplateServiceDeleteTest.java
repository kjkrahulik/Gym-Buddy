package com.gymbuddy.app.Service;

import com.gymbuddy.app.Repositories.WorkoutTemplateRepository;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pure unit tests — no Spring context, no database, just Mockito
@ExtendWith(MockitoExtension.class)
class WorkoutTemplateServiceDeleteTest {

    @Mock
    private WorkoutTemplateRepository workoutTemplateRepository;

    @InjectMocks
    private WorkoutTemplateService workoutTemplateService;

    // ==================== deleteWorkoutTemplate ====================

    @Test
    @DisplayName("BP-1: Deleting an existing template calls repository delete with the correct template")
    void deleteWorkoutTemplate_exists_callsRepositoryDelete() {
        WorkoutTemplate template = new WorkoutTemplate("Push Day", "Chest, shoulders, triceps");
        template.setListID(1L);
        when(workoutTemplateRepository.findById(1L)).thenReturn(Optional.of(template));

        workoutTemplateService.deleteWorkoutTemplate(1L);

        verify(workoutTemplateRepository).delete(template);
    }

    @Test
    @DisplayName("BP-2: Deleting a non-existent template throws IllegalArgumentException and skips delete")
    void deleteWorkoutTemplate_notFound_throwsAndSkipsDelete() {
        when(workoutTemplateRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> workoutTemplateService.deleteWorkoutTemplate(99L));

        assertTrue(ex.getMessage().contains("99"));
        verify(workoutTemplateRepository, never()).delete(any(WorkoutTemplate.class));
    }
}
