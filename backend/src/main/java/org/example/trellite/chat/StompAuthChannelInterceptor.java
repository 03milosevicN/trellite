package org.example.trellite.chat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.auth.jwt.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;


    @Override
    public Message<?> preSend(
            @NonNull Message<?> message,
            @NonNull MessageChannel channel
    ) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            var authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("WS CONNECT rejected - missing or invalid Authorization header.");
                throw new IllegalArgumentException("Missing auth token.");
            }

            // For JWT extraction - "Bearer "...
            String token = authHeader.substring(7);

            try {
                String email = jwtService.extractUsername(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email, null, List.of()
                );

                accessor.setUser(authentication);
                log.info("WS CONNECT successful for user {}", email);

            } catch (Exception e) {
                log.warn("WS CONNECT rejected - invalid token: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid token.");
            }

        }

        return message;
    }

}
