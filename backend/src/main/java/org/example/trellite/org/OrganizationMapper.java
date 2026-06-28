package org.example.trellite.org;

import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    Organization toModel(OrganizationRequest request);
    OrganizationResponse toResponse(Organization model);
}
