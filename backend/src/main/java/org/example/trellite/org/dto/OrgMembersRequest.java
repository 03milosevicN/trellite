package org.example.trellite.org.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.trellite.common.Role;

@Data
public class OrgMembersRequest {
    @NotBlank
    private Long userId;
    @NotBlank
    private Long orgId;
    @NotBlank(message = "Request must include a role.")
    private Role role;
}
