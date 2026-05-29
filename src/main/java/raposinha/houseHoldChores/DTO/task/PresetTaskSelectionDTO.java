package raposinha.houseHoldChores.DTO.task;

public record PresetTaskSelectionDTO(
        Long id,
        String title,
        int frequency,
        String categoryName,
        boolean isCustom,
        String icon,
        String colorCode
) {}