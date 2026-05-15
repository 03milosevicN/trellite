package org.example.trellite.user;

import org.example.trellite.user.dto.UserRequest;
import org.example.trellite.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(req));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long userId,
            @RequestBody UserRequest req
    ) {
        return ResponseEntity.ok(userService.update(userId, req));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> patch(
            @PathVariable Long userId,
            @RequestBody UserRequest req
    ) {
        return ResponseEntity.ok(userService.patch(userId, req));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

}
