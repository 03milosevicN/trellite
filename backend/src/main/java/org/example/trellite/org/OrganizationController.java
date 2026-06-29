package org.example.trellite.org;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Organization")
@RequestMapping("/api/orgs")
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

    @PostMapping("/{id}")
    public ResponseEntity<Void> joinOrg(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        service.joinOrg(id, user);
        return ResponseEntity.noContent().build();
    }

}
