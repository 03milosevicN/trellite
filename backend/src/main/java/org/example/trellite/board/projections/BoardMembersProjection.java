package org.example.trellite.board.projections;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

public interface BoardMembersProjection {
    @Field(name = "_id")
    ObjectId getId();
    @Field(name = "members")
    List<Long> getMembers();
}
