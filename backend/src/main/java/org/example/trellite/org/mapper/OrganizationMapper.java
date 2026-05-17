package org.example.trellite.org.mapper;

import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.org.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    @Mapping(target = "ownedBy", ignore = true)
    Organization toModel(OrganizationRequest request);
    @Mapping(target = "ownedBy", source = "ownedBy.userId")
    OrganizationResponse toResponse(Organization model);
}
