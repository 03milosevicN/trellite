package org.example.trellite.member;

import org.example.trellite.member.dto.MemberRequest;
import org.example.trellite.member.dto.MemberResponse;
import org.example.trellite.org.OrganizationMapper;
import org.example.trellite.user.UserMapperProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {UserMapperProto.class, OrganizationMapper.class }
)
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "org.id", source = "orgId")
    Member toEntity(MemberRequest request);

    @Mapping(target = "memberId", source = "entity.id")
    @Mapping(target = "userResponse", source = "user")
    @Mapping(target = "orgResponse", source = "org")
    MemberResponse toResponse(Member entity);

}
