package faang.school.postservice.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDto {
    private long id;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 128)
    private String title;
}
