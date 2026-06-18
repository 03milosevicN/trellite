package org.example.trellite.org.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trellite.org.dto.NameUpdateRequest;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.org.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orgs")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;


    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok( service.getById(id) );
    }


    @PostMapping
    public ResponseEntity<OrganizationResponse> create(
            @RequestBody @Valid OrganizationRequest req,
            @AuthenticationPrincipal AuthenticatedUser creator
    ) {
        var response = service.save(req, creator.getUser());
        return ResponseEntity.status( HttpStatus.CREATED ).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationResponse> patch(
            @PathVariable Long id,
            @RequestBody NameUpdateRequest req,
            @AuthenticationPrincipal AuthenticatedUser creator
    ) {
        var response = service.patch(id, req, creator.getUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser creator
    ) {
        service.delete(id, creator.getUser());
        return ResponseEntity.noContent().build();
    }

}
