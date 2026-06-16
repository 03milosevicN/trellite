package org.example.trellite.org.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class OrganizationRequest {
    @NotBlank
    private String name;
    @NotBlank
    private Instant createdAt;
}
