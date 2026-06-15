package org.example.trellite.auth;

import jakarta.validation.Valid;
import org.example.trellite.auth.dto.LoginRequest;
import org.example.trellite.auth.dto.LoginResponse;
import org.example.trellite.auth.dto.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public void register(@Valid RegistrationRequest req) {
        //TODO: Implement method stub.
    }

    public LoginResponse authenticate(LoginRequest req) {
        //TODO: Implement method stub.
        return null;
    }

}
