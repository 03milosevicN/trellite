package org.example.trellite.checklist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.trellite.item.dto.ItemRequest;
import java.util.List;

@Data
public class ChecklistRequest {
    @NotBlank(message = "Title shouldn't be blank.")
    private String title;
    private Boolean isCompleted;
    private List<ItemRequest> items;
}
