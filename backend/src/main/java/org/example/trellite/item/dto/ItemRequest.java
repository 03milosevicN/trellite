package org.example.trellite.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemRequest {
    @NotBlank(message = "Checklist ID cannot be blank.")
    private String checklistId;
    @NotBlank(message = "Field should not be blank.")
    private String task;
}
