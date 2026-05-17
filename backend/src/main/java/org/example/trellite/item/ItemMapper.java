package org.example.trellite.item;

import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.item.dto.ItemRequest;
import org.example.trellite.item.dto.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class} )
public interface ItemMapper {

    @Mapping(
            target = "id",
            ignore = true
    )
    Item toModel(ItemRequest request);

    @Mapping(
            target = "id",
            source = "id",
            qualifiedByName = "objectIdToString"
    )
    ItemResponse toResponse(Item model);

}
