package org.example.trellite.board.projections;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public interface BoardIdProjection {
    @Field(name = "_id")
    ObjectId getId();
}
