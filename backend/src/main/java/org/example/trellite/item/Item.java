package org.example.trellite.item;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Item {

    @Field(name = "_id")
    private ObjectId id;

    private ObjectId checklistId;

    @Field(name = "task")
    private String task;

}
