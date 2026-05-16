package org.example.trellite.board;

import jakarta.persistence.Id;
import lombok.Data;
import org.example.trellite.boardList.BoardList;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import java.time.Instant;
import java.util.List;

@Document(collection = "boards")
@Data
public class Board {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "org_id")
    private Long orgId;

    @Field(name = "title")
    private String title;

    @Field(name = "members")
    private List<Long> members;

    @Field(name = "created_at")
    private Instant createdAt;

    @Field(name = "archived")
    private Boolean archived;


    @DocumentReference(lookup = "{ 'boardId' : ?#{#self._id} }")
    private List<BoardList> boardLists;

}
