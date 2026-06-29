package org.example.trellite.org.dto;

import lombok.Data;
import org.example.trellite.member.Member;
import org.example.trellite.user.User;

import java.time.Instant;

@Data
public class OrganizationResponse {
    private Long orgId;
    private String name;
    private Instant createdAt;
    private User ownedBy;
}
