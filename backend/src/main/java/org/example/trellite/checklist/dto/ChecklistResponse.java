package org.example.trellite.checklist.dto;

import lombok.Data;
import org.example.trellite.item.Item;
import java.util.List;

@Data
public class ChecklistResponse {
    private String id;
    private String cardId;
    private String title;
    private Boolean isCompleted;
    private List<Item> items;
}
