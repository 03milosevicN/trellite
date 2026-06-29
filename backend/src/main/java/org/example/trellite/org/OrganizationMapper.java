package org.example.trellite.org;

import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ownedBy", ignore = true)
    Organization toModel(OrganizationRequest request);

    @Mapping(target = "orgId", source = "model.id")
    @Mapping(target = "ownedBy", source = "model.ownedBy")
    OrganizationResponse toResponse(Organization model);
}
