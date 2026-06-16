package org.example.trellite.org.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.trellite.auth.AuthenticatedUser;
import org.example.trellite.common.BaseController;
import org.example.trellite.org.dto.NameUpdateRequest;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.org.service.OrganizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orgs")
@RequiredArgsConstructor
public class OrganizationController implements BaseController<OrganizationRequest, OrganizationResponse, Long> {

    private final OrganizationServiceImpl organizationService;


    @Override
    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAll() {
        return ResponseEntity.ok(organizationService.getAll());
    }

    @Override
    @GetMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable Long orgId) {
        return ResponseEntity.ok(organizationService.getById(orgId));
    }

    @Override
    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@RequestBody OrganizationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.save(req));
    }

    // PROTO //
    // TODO: Finish method implementation.
    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrg(
            @RequestBody @Valid OrganizationRequest req,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        var response = organizationService.saveProto(req, authenticatedUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> patchName(
            @PathVariable Long orgId,
            @RequestBody NameUpdateRequest req,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        var response = organizationService.patchNameProto(orgId, req, authenticatedUser.getUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orgId}")
    public ResponseEntity<Void> deleteOrg(
            @PathVariable Long orgId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        organizationService.deleteProto(orgId, authenticatedUser.getUser());
        return ResponseEntity.noContent().build();
    }
    // PROTO //


    @Deprecated
    @Override
    @PutMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> update(
            @PathVariable Long orgId,
            @RequestBody OrganizationRequest req
    ) {
        return ResponseEntity.ok(organizationService.update(orgId, req));
    }

    @Override
    @DeleteMapping("/{orgId}")
    public ResponseEntity<Void> delete(@PathVariable Long orgId) {
        organizationService.delete(orgId);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/{orgId}")
    public ResponseEntity<OrganizationResponse> patchName(
            @PathVariable Long orgId,
            @RequestBody NameUpdateRequest req
    ) {
        return ResponseEntity.ok(organizationService.patchName(orgId, req));
    }

}
