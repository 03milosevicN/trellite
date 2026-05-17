package org.example.trellite.checklist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.trellite.item.dto.ItemResponse;
import java.util.List;

@Data
public class ChecklistResponse {
    private String id;
    @JsonIgnore
    private String cardId;
    private String title;
    private Boolean isCompleted;
    private List<ItemResponse> items;
}
