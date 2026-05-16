package org.example.trellite.checklist;

import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.example.trellite.item.ItemMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ItemMapper.class })
public interface ChecklistMapper {

    Checklist toModel(ChecklistRequest request);

    ChecklistResponse toResponse(Checklist model);

}
