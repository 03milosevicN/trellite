package org.example.trellite.checklist;

import org.example.trellite.checklist.dto.ChecklistRequest;
import org.example.trellite.checklist.dto.ChecklistResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChecklistMapper {

    Checklist toModel(ChecklistRequest request);

    ChecklistResponse toResponse(Checklist model);

}
