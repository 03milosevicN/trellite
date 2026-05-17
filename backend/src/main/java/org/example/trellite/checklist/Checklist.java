package org.example.trellite.checklist;

import lombok.Data;
import org.bson.types.ObjectId;
import org.example.trellite.item.Item;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Data
public class Checklist {

    @Field(name = "_id")
    private ObjectId id;
    
    private ObjectId cardId;

    @Field(name = "title")
    private String title;

    @Field(name = "is_completed")
    private Boolean isCompleted;

    @Field(name = "items")
    private List<Item> items;

}
