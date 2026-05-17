package org.example.trellite.checklist;

import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.item.ItemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ItemMapper.class, ObjectIdMapper.class})
public interface ChecklistMapper {

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(target = "cardId", ignore = true)
    Checklist toModel(ChecklistRequest request);

    @Mapping(
            target = "id",
            source = "id",
            qualifiedByName = "objectIdToString"
    )
    @Mapping(
            target = "cardId",
            source = "cardId",
            qualifiedByName = "objectIdToString"
    )
    ChecklistResponse toResponse(Checklist model);

}
