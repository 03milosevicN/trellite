package org.example.trellite.item.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ItemResponse {
    @Field("_id")
    private String id;
    private String task;
}
