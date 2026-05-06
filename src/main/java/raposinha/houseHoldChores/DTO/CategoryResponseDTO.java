package raposinha.houseHoldChores.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;

    @NotBlank
    private String title; // e.g., "Kitchen"

    private String description; // e.g., "Chores related to cooking and cleaning dishes"
}