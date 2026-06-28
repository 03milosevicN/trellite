package org.example.trellite.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.auth.dto.LoginRequest;
import org.example.trellite.auth.dto.LoginResponse;
import org.example.trellite.auth.dto.RegistrationRequest;
import org.example.trellite.auth.jwt.JwtService;
import org.example.trellite.user.User;
import org.example.trellite.user.UserMapper;
import org.example.trellite.user.UserRepository;
import org.example.trellite.user.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserResponse register(@Valid RegistrationRequest req) {

        var user = User
                .builder()
                .firstName( req.getFirstName() )
                .lastName( req.getLastName() )
                .email( req.getEmail() )
                .password( passwordEncoder.encode(req.getPassword()) )
                .accountLocked( false )
                .enabled( false )
                .createdAt( Instant.now() )
                .lastModifiedAt( Instant.now() )
                .build();

        log.info("Successfully registered user {} @ {}", req.getEmail(), Instant.now());
        var saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    public LoginResponse authenticate(LoginRequest req) {
        var auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        log.info("Authenticating {}", req.getEmail() );

        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        if (user == null) {
            throw new NullPointerException("User principal is null.");
        }
        claims.put("email", user.getEmail());

        var jwtToken = jwtService.generateToken(claims, (UserDetails)auth.getPrincipal() );
        log.info("Token generated for {}", req.getEmail());

        var userQuery = userRepository
                .findById(user.getId())
                .orElseThrow( () -> new UsernameNotFoundException("Failed to query user principal User not found."));

        userQuery.setEnabled(true);
        log.info("{}'s account enabled.", req.getEmail());

        return LoginResponse
                .builder()
                .token( jwtToken )
                .build();
    }

}
