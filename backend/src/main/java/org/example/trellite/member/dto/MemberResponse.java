package org.example.trellite.member.dto;

import lombok.Data;
import org.example.trellite.common.RoleType;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.user.dto.UserResponse;

@Data
public class MemberResponse {

    private Long memberId;
    private UserResponse userResponse;
    private OrganizationResponse orgResponse;
    private RoleType role;

}
