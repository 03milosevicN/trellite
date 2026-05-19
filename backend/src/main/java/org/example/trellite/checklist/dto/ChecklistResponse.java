package org.example.trellite.checklist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.trellite.item.dto.ItemResponse;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Data
public class ChecklistResponse {
    @Field("_id")
    private String id;
    private String title;
    @Field("is_completed")
    private Boolean isCompleted;
    private List<ItemResponse> items;
    @JsonIgnore
    private String cardId;
}
