package org.example.trellite.org.dto;

import lombok.Data;
import org.example.trellite.common.Role;

@Data
public class OrgMembersResponse {
    private Long orgMembersId;
    private Long userId;
    private Long orgId;
    private Role role;
}
