package org.example.trellite.org.controller;

import org.example.trellite.org.dto.OrgMembersRequest;
import org.example.trellite.org.dto.OrgMembersResponse;
import org.example.trellite.org.service.OrgMembersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/org-members")
public class OrgMembersController {

    private final OrgMembersServiceImpl orgMembersService;

    @Autowired
    public OrgMembersController(OrgMembersServiceImpl orgMembersService) {
        this.orgMembersService = orgMembersService;
    }


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

    @DeleteMapping("/{orgMembersId}")
    public ResponseEntity<Void> delete(@PathVariable Long orgMembersId) {
        orgMembersService.delete(orgMembersId);
        return ResponseEntity.noContent().build();
    }

}
