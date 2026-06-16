package org.example.trellite.org.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class OrganizationResponse {
    private Long orgId;
    private String name;
    private Instant createdAt;
}
