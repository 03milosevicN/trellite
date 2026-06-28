package org.example.trellite.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.trellite.common.RoleType;

@Data
public class MemberRequest {

    @NotEmpty(message = "userId field cannot be empty.")
    @NotNull(message = "userId field cannot be null.")
    private Long userId;

    @NotEmpty(message = "orgId field cannot be empty.")
    @NotNull(message = "orgId field cannot be null.")
    private Long orgId;

    @NotEmpty(message = "role field cannot be empty.")
    @NotNull(message = "role field cannot be null.")
    private RoleType role;

}
