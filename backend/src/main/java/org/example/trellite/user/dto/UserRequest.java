package org.example.trellite.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email(message = "Please provide a valid email address.")
    private String email;
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters.")
    private String password;
}
