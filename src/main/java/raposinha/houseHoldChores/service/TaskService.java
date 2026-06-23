package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.task.*;
import raposinha.houseHoldChores.entities.*;
import raposinha.houseHoldChores.exception.BadRequestException;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.exception.UnauthorizedException;
import raposinha.houseHoldChores.repositories.*;
import org.springframework.data.domain.Pageable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskService {

    private PresetTaskRepo presetRepo;
    private UserRepo userRepo;
    private GroupRepo groupRepo;
    private TaskRepo taskRepo;
    private CategoryRepo categoryRepo;


    public List<PresetTaskSelectionDTO> getAvailablePresetsForUser(User requester) {
        // the user must belong to a group to query templates
        if (requester.getGroup() == null) {
            throw new BadRequestException("You must belong to a household group to view task templates.");
        }
        Long groupId = requester.getGroup().getId();
        List<PresetTask> availablePresets = presetRepo.findAvailablePresetsForGroup(groupId);

        return availablePresets.stream()
                .map(preset -> {
                    Category category = preset.getCategory(); // Grab Category reference

                    return new PresetTaskSelectionDTO(
                            preset.getId(),
                            preset.getTitle(),
                            preset.getFrequency(),
                            category != null ? category.getName() : "General",
                            preset.getGroup() != null,
                            category != null ? category.getIcon() : "circle-check",
                            category != null ? category.getColorCode() : "#FFD700"
                    );
                })
                .toList();
    }

    @Transactional
    public TaskResponseDTO createTaskFromPreset(CreateTaskFromPresetDTO dto, User loggedInUser) {
        if (dto.presetId() == null) {
            throw new BadRequestException("Preset ID must not be null.");
        }
        PresetTask preset = presetRepo.findById(dto.presetId())
                .orElseThrow(() -> new NotFoundException("Preset task not found"));

        if (loggedInUser.getGroup() == null) {
            throw new BadRequestException("You must belong to a household group to create chores.");
        }
        Group group = loggedInUser.getGroup();

        if (dto.dueDate() != null && dto.dueDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Due date cannot be in the past.");
        }
        User assignedUser = dto.assignedUserId() != null
                ? userRepo.findById(dto.assignedUserId())
                .orElseThrow(() -> new NotFoundException("Assigned user not found"))
                : loggedInUser;

        Task newTask = new Task();
        newTask.setTitle(preset.getTitle());
        newTask.setCategory(preset.getCategory());
        newTask.setFrequency(dto.frequency() > 0 ? dto.frequency() : preset.getFrequency());
        newTask.setAssignedTo(assignedUser);
        newTask.setGroup(group);
        newTask.setDueDate(dto.dueDate());
        newTask.setCompleted(false);
        newTask.setSourcePreset(preset);

        Task savedTask = taskRepo.save(newTask);
        generateOccurrencesForTask(savedTask);
        return convertToResponseDTO(savedTask);
    }

    @Transactional
    public TaskResponseDTO createPersonalizedTask(CreatePersonalizedTaskRequestDTO dto) {
        Group group = groupRepo.findById(dto.getGroupId()).orElseThrow();
        Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow();

        // add task to the preset_task table with the group_id, so only member of this group can view this task
        PresetTask customPreset = new PresetTask();
        customPreset.setTitle(dto.getTitle());
        customPreset.setCategory(category);
        customPreset.setGroup(group); // links it ONLY to this household
        customPreset.setFrequency(dto.getFrequency());
        presetRepo.save(customPreset);

        // saves as a live task too
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setCategory(category);
        task.setGroup(group);
        task.setDueDate(dto.getDueDate());
        task.setFrequency(dto.getFrequency());
        task.setCompleted(false);

        if (dto.getAssignedUserId() != null) {
            User user = userRepo.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new NotFoundException("User not found with ID: " + dto.getAssignedUserId()));
            task.setAssignedTo(user);
        } else {
            task.setAssignedTo(null); // explicitly unassigned
        }
        Task savedTask = taskRepo.save(task);
        generateOccurrencesForTask(savedTask);
        return convertToResponseDTO(taskRepo.save(task));
    }

    public TaskResponseDTO assignUserToTask(UUID userToAssign, Long taskId, User requester) {
        //  find task
        Task foundTask = taskRepo.findById(taskId).orElseThrow(() -> new NotFoundException("Task with id "+ taskId + " was not found."));
        // from this foundTask, I get the groupId, and search for the owner.
        Group taskGroup = foundTask.getGroup();
        if (taskGroup == null) {
            throw new BadRequestException("This task does not belong to any household group.");
        }
        // check if requester is admin of group
        if (!taskGroup.getOwner().getId().equals(requester.getId())) {
            throw new UnauthorizedException("Only the group administrator can assign tasks.");
        }

        // find user to be assigned
        User worker = userRepo.findById(userToAssign)
                .orElseThrow(() -> new NotFoundException("User to assign not found."));

        // check if user belongs to group
        if (worker.getGroup() == null || !worker.getGroup().getId().equals(taskGroup.getId())) {
            throw new BadRequestException("The assigned user does not belong to this household group.");
        }

        // assign to user
        foundTask.setAssignedTo(worker);
        return convertToResponseDTO(taskRepo.save(foundTask));

    }

    @Transactional
    public TaskResponseDTO updateDueDate(Long taskId, UpdateDueDateDTO dto, User requester) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        // only admin can change deadlines
        if (!task.getGroup().getOwner().getId().equals(requester.getId())) {
            throw new UnauthorizedException("Only the group admin can change deadlines.");
        }

        task.setDueDate(dto.dueDate());
        return convertToResponseDTO(taskRepo.save(task));
    }

    @Transactional
    public TaskResponseDTO updateFrequency(Long taskId, UpdateFrequencyDTO dto, User requester) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getGroup().getOwner().getId().equals(requester.getId())) {
            throw new UnauthorizedException("Only the group admin can change task frequencies.");
        }

        task.setFrequency(dto.frequency());
        Task savedTask = taskRepo.saveAndFlush(task); // 👈 flush immediately so delete sees the updated state

        if (savedTask.getParentTask() == null) {
            taskRepo.deleteFutureOccurrences(taskId, LocalDateTime.now());
            taskRepo.flush();
            generateOccurrencesForTask(savedTask);
        }

        return convertToResponseDTO(savedTask);
    }

    @Transactional
    public TaskResponseDTO markAsCompleted(Long taskId, User requester) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        // Security check: Requester must belong to the same household group as the task
        if (requester.getGroup() == null || !requester.getGroup().getId().equals(task.getGroup().getId())) {
            throw new UnauthorizedException("You do not belong to this household group.");
        }

        task.setCompleted(!task.isCompleted());
        return convertToResponseDTO(taskRepo.save(task));
    }

    public List<TaskResponseDTO> getTasksAssignedToUser(UUID userId, User requester) {
        //can only view tasks if requester shares the same household group
        User targetUser = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (requester.getGroup() == null || !requester.getGroup().getId().equals(targetUser.getGroup().getId())) {
            throw new UnauthorizedException("You can only view tasks of members in your own household.");
        }

        return taskRepo.findByAssignedToId(userId).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }


    public List<TaskResponseDTO> getTasksByGroupAndRange(User user, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        if (user.getGroup() == null) {
            throw new BadRequestException("You do not belong to any household group.");
        }

        Long groupId = user.getGroup().getId();

        List<Task> tasks = taskRepo.findByGroup_IdAndDueDateBetween(groupId, start, end);
        System.out.println("tasks by group and due date between" + tasks);
        return tasks.stream()
                .map(this::convertToResponseDTO) // Your internal DTO mapper helper
                .toList();
    }

    private TaskResponseDTO convertToResponseDTO(Task task) {
        Category category = task.getCategory();
        User assignedUser = task.getAssignedTo();
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getCategory() != null ? task.getCategory().getName() : null,
                task.getDueDate(),
                task.isCompleted(),
                assignedUser != null ? assignedUser.getId() : null,
                assignedUser != null ? assignedUser.getUsername() : "Unassigned",
                task.getFrequency(),
                category != null ? category.getIcon() : "circle-check",
                category != null ? category.getColorCode() : "#FFD700",
                assignedUser != null ? assignedUser.getAvatarUrl() : "https://res.cloudinary.com/dga90puif/image/upload/q_auto/f_auto/v1778151410/Screenshot_from_2026-05-07_12-53-38_tch5d6.png"
        );
    }

    @Transactional
    public void generateUpcomingOccurrences() {
        List<Task> templates = taskRepo.findAllUserCreatedTasks();
        System.out.println("Scheduler found {} templates"+ templates.size());
        templates.forEach(this::generateOccurrencesForTask);
    }

    @Transactional
    public void generateOccurrencesForTask(Task template) {
        if (template.getFrequency() <= 0 || template.getDueDate() == null) return;

        LocalDateTime horizon = LocalDateTime.now().plusMonths(2);

        Pageable limit = PageRequest.of(0, 1);
        List<Task> latest = taskRepo.findOccurrencesSortedByDueDate(template.getId(), limit);

        LocalDateTime nextDueDate = latest.stream().findFirst()
                .map(t -> t.getDueDate().plusDays(template.getFrequency()))
                .orElse(template.getDueDate().plusDays(template.getFrequency()));

        List<Task> occurrences = new ArrayList<>();
        while (nextDueDate.isBefore(horizon)) {
            Task occurrence = new Task();
            occurrence.setTitle(template.getTitle());
            occurrence.setCategory(template.getCategory());
            occurrence.setGroup(template.getGroup());
            occurrence.setFrequency(template.getFrequency());
            occurrence.setAssignedTo(template.getAssignedTo());
            occurrence.setDueDate(nextDueDate);
            occurrence.setCompleted(false);
            occurrence.setParentTask(template);
            occurrences.add(occurrence);
            nextDueDate = nextDueDate.plusDays(template.getFrequency());
        }
        taskRepo.saveAll(occurrences);
    }
}
