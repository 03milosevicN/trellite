package org.example.trellite.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.trellite.board.projections.BoardIdProjection;
import org.example.trellite.common.RoleType;
import org.example.trellite.member.dto.MemberResponse;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/by-org/{orgId}")
    public ResponseEntity<List<MemberResponse>> getOrgMembers(@PathVariable Long orgId) {
        return ResponseEntity.ok(memberService.getOrgMembers(orgId));
    }

    @GetMapping("/user/{userId}/adminships")
    public ResponseEntity<List<OrganizationResponse>> getMyAdminships(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(memberService.getMyAdminships(userId));
    }

    @GetMapping("/user/{userId}/memberships")
    public ResponseEntity<List<OrganizationResponse>> getMyMemberships(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(memberService.getMyMemberships(userId));
    }

    @GetMapping("/organizations/{orgId}/membership")
    public ResponseEntity<OrganizationResponse> getMyMembership(
            @PathVariable Long orgId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(memberService.getMyMembership(user.getId(), orgId));
    }

    @GetMapping("/organizations/{orgId}/boards/memberships")
    public ResponseEntity<List<String>> getMyBoardMemberships(
            @PathVariable Long orgId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(memberService.getMyBoardMemberships(orgId, user.getId()));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasRole(
            @RequestParam String email,
            @RequestParam Long orgId,
            @RequestParam RoleType role
    ) {
        return ResponseEntity.ok(memberService.hasRole(email, orgId, role));
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