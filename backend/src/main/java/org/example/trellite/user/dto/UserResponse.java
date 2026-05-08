package org.example.trellite.user.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
