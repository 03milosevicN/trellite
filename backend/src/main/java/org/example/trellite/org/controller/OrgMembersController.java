package org.example.trellite.org.controller;

import lombok.RequiredArgsConstructor;
import org.example.trellite.org.dto.*;
import org.example.trellite.org.service.OrgMembersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/org-members")
@RequiredArgsConstructor
public class OrgMembersController {

    private final OrgMembersServiceImpl orgMembersService;


    @GetMapping("/{orgMembersId}")
    public ResponseEntity<OrgMembersResponse> getById(@PathVariable Long orgMembersId) {
        return ResponseEntity.ok(orgMembersService.getById(orgMembersId));
    }

    @GetMapping
    public ResponseEntity<List<OrgMembersResponse>> getAll() {
        return ResponseEntity.ok(orgMembersService.getAll());
    }

    @PostMapping
    public ResponseEntity<OrgMembersResponse> create(@RequestBody OrgMembersRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orgMembersService.save(req));
    }

    @PutMapping("/{orgMembersId}")
    public ResponseEntity<OrgMembersResponse> update(
            @PathVariable Long orgMembersId,
            @RequestBody OrgMembersRequest req
    ) {
        return ResponseEntity.ok(orgMembersService.update(orgMembersId, req));
    }

    @PatchMapping("/{orgMembersId}/user")
    public ResponseEntity<OrgMembersResponse> updateUser(
            @PathVariable Long orgMembersId,
            @RequestBody UserTransferRequest req
    ) {
        return ResponseEntity.ok(orgMembersService.updateUser(orgMembersId, req));
    }

    @PatchMapping("/{orgMembersId}/org")
    public ResponseEntity<OrgMembersResponse> updateOrg(
            @PathVariable Long orgMembersId,
            @RequestBody OrganizationTransferRequest req
            ) {
        return ResponseEntity.ok(orgMembersService.updateOrg(orgMembersId, req));
    }

    @PatchMapping("/{orgMembersId}")
    public ResponseEntity<OrgMembersResponse> updateRole(
            @PathVariable Long orgMembersId,
            @RequestBody RoleUpdateRequest req
    ) {
        return ResponseEntity.ok(orgMembersService.updateRole(orgMembersId, req));
    }

    @DeleteMapping("/{orgMembersId}")
    public ResponseEntity<Void> delete(@PathVariable Long orgMembersId) {
        orgMembersService.delete(orgMembersId);
        return ResponseEntity.noContent().build();
    }

}
