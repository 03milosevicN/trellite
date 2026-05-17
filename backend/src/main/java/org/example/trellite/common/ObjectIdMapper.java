package org.example.trellite.common;

import org.bson.types.ObjectId;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdMapper {

    @Named("stringToObjectId")
    public ObjectId stringToObjectId(String id) {
        return id == null ? null : new ObjectId(id);
    }

    @Named("objectIdToString")
    public String objectIdToString(ObjectId id) {
        return id == null ? null : id.toHexString();
    }

}
