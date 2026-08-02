package org.example.trellite.common;

import org.bson.types.ObjectId;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdMapper {

    @Named("stringToObjectId")
    public ObjectId stringToObjectId(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid ObjectId: " + id);
        }

        return new ObjectId(id);
    }

    @Named("objectIdToString")
    public String objectIdToString(ObjectId id) {
        return id == null ? null : id.toHexString();
    }

}
