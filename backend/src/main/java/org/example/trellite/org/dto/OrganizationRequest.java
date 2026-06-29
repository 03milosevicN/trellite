package org.example.trellite.org.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.Instant;

@Data
public class OrganizationRequest {
    @NotBlank
    private String name;
    @NotNull(message = "Created at field cannot be null.")
    @PastOrPresent(message = "Created at date must be in the past or present.")
    private Instant createdAt;
}
