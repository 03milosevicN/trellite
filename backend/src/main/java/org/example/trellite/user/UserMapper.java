package org.example.trellite.user;

import org.example.trellite.auth.dto.LoginRequest;
import org.example.trellite.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * TODO: Upon integration, rename to UserMapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountLocked", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    User toEntity(LoginRequest request);

    @Mapping(target = "userId", source = "entity.id")
    UserResponse toResponse(User entity);

}

