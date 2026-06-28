package org.example.trellite.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trellite.common.BaseController;
import org.example.trellite.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Deprecated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements BaseController<UserRequest, UserResponse, Long> {

    private final UserServiceImpl userService;


    @Override
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(req));
    }

    @Override
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long userId,
            @Valid @RequestBody UserRequest req
    ) {
        return ResponseEntity.ok(userService.update(userId, req));
    }

    @Override
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> patchPassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordUpdateRequest req
    ) {
        return ResponseEntity.ok(userService.patchPassword(userId, req));
    }


}
