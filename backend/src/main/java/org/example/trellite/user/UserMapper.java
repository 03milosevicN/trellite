package org.example.trellite.user;

import org.example.trellite.user.dto.UserRequest;
import org.example.trellite.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserRequest request);
    UserResponse toResponse(User model);
}
