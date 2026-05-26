package raposinha.houseHoldChores.DTO.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raposinha.houseHoldChores.DTO.task.PresetTaskDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithTasksResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String icon;
    private String colorCode;
    private List<PresetTaskDTO> tasks;
}