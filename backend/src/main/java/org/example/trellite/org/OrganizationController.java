package org.example.trellite.org;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trellite.org.dto.NameUpdateRequest;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.user.User;
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
            @AuthenticationPrincipal User creator
    ) {
        var response = service.save(req, creator);
        return ResponseEntity.status( HttpStatus.CREATED ).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User creator
    ) {
        service.delete(id, creator);
        return ResponseEntity.noContent().build();
    }

}
