package org.example.trellite.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotNull(message = "First name field cannot be null.")
    @NotEmpty(message = "First name field cannot be empty.")
    private String firstName;

    @NotNull(message = "First name field cannot be null.")
    @NotEmpty(message = "First name field cannot be empty.")
    private String lastName;

    @Email(message = "Invalid email format.")
    @NotEmpty(message = "E-mail field cannot be empty.")
    @NotNull(message = "E-mail field cannot be null.")
    private String email;

    @Size(min = 8, message = "Password field should be at least 8 characters long.")
    @NotEmpty(message = "Password field cannot be empty.")
    @NotNull(message = "Password field cannot be null.")
    private String password;

}
