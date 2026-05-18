package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.*;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // when a user selects a task from the preset tasks
    @PostMapping("/preset")
    public ResponseEntity<TaskResponseDTO> createFromPreset(
            @Valid @RequestBody CreateTaskFromPresetDTO request) {
        TaskResponseDTO response = taskService.createTaskFromPreset(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // when a user types in their own custom chore
    @PostMapping("/personalized")
    public ResponseEntity<TaskResponseDTO> createPersonalized(
            @Valid @RequestBody CreatePersonalizedTaskRequestDTO request) {
        TaskResponseDTO response = taskService.createPersonalizedTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // assign task to someone
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<String> assignUserToExistingTask(
            @PathVariable Long taskId,
            @Validated @RequestBody AssignUserDTO body,
            @AuthenticationPrincipal User requester) {

        String message = taskService.assignUserToTask(body.userId(), taskId, requester);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{taskId}/due-date")
    public ResponseEntity<TaskResponseDTO> changeDueDate(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateDueDateDTO dto,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.updateDueDate(taskId, dto, requester));
    }

    @PatchMapping("/{taskId}/frequency")
    public ResponseEntity<TaskResponseDTO> changeFrequency(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateFrequencyDTO dto,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.updateFrequency(taskId, dto, requester));
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponseDTO> completeTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.markAsCompleted(taskId, requester));
    }

    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(
            @PathVariable UUID userId,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.getTasksAssignedToUser(userId, requester));
    }

}
