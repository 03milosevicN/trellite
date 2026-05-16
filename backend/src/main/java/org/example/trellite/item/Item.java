package org.example.trellite.item;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Item {

    @Field(name = "_id")
    private String id;

    private String checklistId;

    @Field(name = "task")
    private String task;

}
