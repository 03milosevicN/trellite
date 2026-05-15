package org.example.trellite.org.controller;

import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.org.service.OrganizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orgs")
public class OrganizationController {

    private final OrganizationServiceImpl organizationService;

    @Autowired
    public OrganizationController(OrganizationServiceImpl organizationService) {
        this.organizationService = organizationService;
    }


    @GetMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable Long orgId) {
        return ResponseEntity.ok(organizationService.getById(orgId));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAll() {
        return ResponseEntity.ok(organizationService.getAll());
    }

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@RequestBody OrganizationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.save(req));
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> update(
            @PathVariable Long orgId,
            @RequestBody OrganizationRequest req
    ) {
        return ResponseEntity.ok(organizationService.update(orgId, req));
    }

    @PatchMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> patch(
            @PathVariable Long orgId,
            @RequestBody OrganizationRequest req
    ) {
        return ResponseEntity.ok(organizationService.patch(orgId, req));
    }

    @DeleteMapping("/{orgId}")
    public ResponseEntity<Void> delete(@PathVariable Long orgId) {
        organizationService.delete(orgId);
        return ResponseEntity.noContent().build();
    }

}
