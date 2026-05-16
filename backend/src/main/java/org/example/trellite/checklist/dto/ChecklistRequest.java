package org.example.trellite.checklist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChecklistRequest {
    @NotBlank(message = "Title shouldn't be blank.")
    private String title;
    private Boolean isCompleted;
}
