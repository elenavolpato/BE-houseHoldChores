package raposinha.houseHoldChores.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raposinha.houseHoldChores.DTO.category.CategoryResponseDTO;
import raposinha.houseHoldChores.DTO.category.CategoryWithTasksResponseDTO;
import raposinha.houseHoldChores.DTO.task.PresetTaskDTO;
import raposinha.houseHoldChores.DTO.task.TaskResponseDTO;
import raposinha.houseHoldChores.entities.Category;
import raposinha.houseHoldChores.entities.PresetTask;
import raposinha.houseHoldChores.entities.User;
import raposinha.houseHoldChores.exception.NotFoundException;
import raposinha.houseHoldChores.repositories.CategoryRepo;
import raposinha.houseHoldChores.repositories.PresetTaskRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final PresetTaskRepo presetTaskRepo;

    public Category createCategory(String name, String desc, String icon) {
        Category c = new Category(name);
        c.setDescription(desc);
        // fallback icon, in case user does not select one
        if(icon == null){ c.setIcon("house");     }
        else {c.setIcon(icon);}
        return c;
    }

    public CategoryResponseDTO saveAndReturnDTO(String name, String desc, String icon) {
        Category category = new Category(name);
        category.setDescription(desc);
        category.setIcon(icon == null ? "house" : icon);

        Category saved = categoryRepo.save(category);

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(saved.getId());
        dto.setTitle(saved.getName()); // Mapping name -> title
        dto.setDescription(saved.getDescription());
        dto.setIcon(saved.getIcon());

        return dto;
    }

    @Transactional
    public CategoryWithTasksResponseDTO getCategoryWithTasks(String name, User user) {
        Category category = categoryRepo.findByName(name)
                .orElseThrow(() -> new NotFoundException("No category with the title '" + name + "' was found."));

        Long userGroupId =  (user.getGroup() != null ? user.getGroup().getId() : null);

        // return only the task in the preset which the group_id is null or the corresponding group_id of the user
        List<PresetTask> matchingPresets = presetTaskRepo.findPresetsByCategoryAndGroup(category.getId(), userGroupId);

        List<PresetTaskDTO> taskDtos = matchingPresets.stream()
                .map(task -> new PresetTaskDTO(
                        category.getId(),
                        task.getId(),
                        task.getTitle(),
                        task.getFrequency(),
                        // groupId (Safely handles null system-wide tasks)
                        task.getGroup() != null ? task.getGroup().getId() : null
                ))
                .toList();

        return new CategoryWithTasksResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIcon(),
                taskDtos
        );
    }
}



