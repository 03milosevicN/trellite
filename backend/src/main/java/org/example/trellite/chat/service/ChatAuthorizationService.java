package org.example.trellite.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.board.BoardRepository;
import org.example.trellite.user.User;
import org.example.trellite.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatAuthorizationService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public boolean isMember(String boardId, String email) {
        var board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            log.warn("Failed to check board membership. Board {} doesn't exist.", board);
            return false;
        }

        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.warn("Failed to check board membership. User {} doesn't exist.", user);
            return false;
        }
        log.info("Checking fetched user with email of {}'s authorities: {}", user.get().getEmail(), user.get().getAuthorities());
        user.get().getAuthorities();

        boolean isAdmin = user.get().getAuthorities().toString().contains("ROLE_ADMIN") || user.get().getAuthorities().toString().contains("ADMIN");
        if (isAdmin) {
            log.info("Access granted to ADMIN user {} for board {}", email, boardId);
            return true;
        }

        var members = board.get().getMembers();
        boolean isMemberOf = members != null && members.contains(user.get().getId());
        if (!isMemberOf) {
            log.warn("User {} tried to access board without a membership to board {}.", email, boardId);
        }

        return isMemberOf;
    }

    public Long resolveUserId(String email) {
        return userRepository
                .findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User with email of " + email + " not found."));
    }

}
