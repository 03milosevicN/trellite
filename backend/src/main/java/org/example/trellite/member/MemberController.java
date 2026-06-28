package org.example.trellite.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.trellite.member.dto.MemberResponse;
import org.example.trellite.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Member")
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> leaveOrg(
            @PathVariable Long id,
            @AuthenticationPrincipal User caller
    ) {
        memberService.leaveOrg(id, caller);
        return ResponseEntity.noContent().build();
    }

}