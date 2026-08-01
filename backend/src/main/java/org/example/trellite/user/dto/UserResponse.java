package org.example.trellite.user.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UserResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Instant createdAt;
}
