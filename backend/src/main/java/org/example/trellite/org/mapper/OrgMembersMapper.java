package org.example.trellite.org.mapper;

import org.example.trellite.org.dto.OrgMembersRequest;
import org.example.trellite.org.dto.OrgMembersResponse;
import org.example.trellite.org.model.OrgMembers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrgMembersMapper {

    @Mapping(target = "user.userId", source = "userId")
    @Mapping(target = "organization.orgId", source = "orgId")
    OrgMembers toModel(OrgMembersRequest request);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "orgId", source = "organization.orgId")
    OrgMembersResponse toResponse(OrgMembers model);
}
