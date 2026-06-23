package raposinha.houseHoldChores.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raposinha.houseHoldChores.DTO.task.*;
import raposinha.houseHoldChores.DTO.user.AssignUserDTO;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    // when a user selects a task from the preset tasks
    // GET /api/tasks/presets
    @GetMapping("/presets")
    public ResponseEntity<List<PresetTaskSelectionDTO>> getAvailablePresets(@AuthenticationPrincipal User user) {
        List<PresetTaskSelectionDTO> presets = taskService.getAvailablePresetsForUser(user);
        return ResponseEntity.ok(presets);
    }


    // POST /api/tasks/create-from-preset
    @PostMapping("/create-from-preset")
    public ResponseEntity<TaskResponseDTO> createTaskFromPreset(
            @Valid @RequestBody CreateTaskFromPresetDTO request,
            @AuthenticationPrincipal User user) { // 👈 Grab session context here

        TaskResponseDTO response = taskService.createTaskFromPreset(request, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // when a user types in their own custom chore
    // POST /api/tasks/personalized
    @PostMapping("/create-task")
    public ResponseEntity<TaskResponseDTO> createPersonalizedTask(
            @Valid @RequestBody CreatePersonalizedTaskRequestDTO request) {
        TaskResponseDTO response = taskService.createPersonalizedTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // assign task to someone
    // PATCH /api/tasks/2/assign
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponseDTO> assignUserToExistingTask(
            @PathVariable Long taskId,
            @Valid @RequestBody AssignUserDTO body,
            @AuthenticationPrincipal User requester) {
        TaskResponseDTO updatedTask = taskService.assignUserToTask(body.userId(), taskId, requester);
        return ResponseEntity.ok(updatedTask);
    }

    //PATCH /api/tasks/1000/due-date
    @PatchMapping("/{taskId}/due-date")
    public ResponseEntity<TaskResponseDTO> changeDueDate(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateDueDateDTO dto,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.updateDueDate(taskId, dto, requester));
    }

    // PATCH /api/tasks/1/frequency
    @PatchMapping("/{taskId}/frequency")
    public ResponseEntity<TaskResponseDTO> changeFrequency(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateFrequencyDTO dto,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.updateFrequency(taskId, dto, requester));
    }

    // PATCH /api/tasks/1/complete
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponseDTO> completeTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.markAsCompleted(taskId, requester));
    }

    // GET /api/tasks/assigned-to/UUID
    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(
            @PathVariable UUID userId,
            @AuthenticationPrincipal User requester) {
        return ResponseEntity.ok(taskService.getTasksAssignedToUser(userId, requester));
    }

    // GET /api/tasks/group/week
    @GetMapping("/group/week")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByGroupAndRange(
            @AuthenticationPrincipal User user,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime start,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime end) {

        List<TaskResponseDTO> tasks = taskService.getTasksByGroupAndRange(user, start, end);
        return ResponseEntity.ok(tasks);
    }
}
