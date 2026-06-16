package org.example.trellite.org.service;

import lombok.RequiredArgsConstructor;
import org.example.trellite.common.ResourceNotFoundException;
import static org.example.trellite.common.Role.ADMIN;
import org.example.trellite.org.dto.NameUpdateRequest;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.org.mapper.OrganizationMapper;
import org.example.trellite.org.model.OrgMembers;
import org.example.trellite.org.repository.OrgMembersRepository;
import org.example.trellite.org.repository.OrganizationRepository;
import org.example.trellite.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;


@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrgMembersRepository membersRepository;
    private final OrganizationMapper organizationMapper;


    public OrganizationResponse getById(Long id) {
        return organizationRepository
                .findByOrgId(id)
                .map(organizationMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
    }


    @Transactional
    public OrganizationResponse save(
            OrganizationRequest req,
            User creator
    ) {
        var model = organizationMapper.toModel(req);
        model.setCreatedAt(Instant.now());
        var saved = organizationRepository.save(model);

        var membership = new OrgMembers();
        membership.setOrganization(model);
        membership.setUser(creator);
        membership.setRole(ADMIN);
        membersRepository.save(membership);

        return organizationMapper.toResponse(saved);
    }

    @Transactional
    public OrganizationResponse patch(
            Long id,
            NameUpdateRequest req,
            User user
    ) {
        var existing = organizationRepository
                .findByOrgId(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
        membersRepository
                .findByOrganizationAndUser(existing, user)
                .filter(m -> m.getRole() == ADMIN)
                .orElseThrow( () -> new AccessDeniedException("Operation not performed by an org. admin."));

        if (req.getName() != null) existing.setName(req.getName());

        return organizationMapper.toResponse(organizationRepository.save(existing));
    }

    @Transactional
    public void delete(
            Long id,
            User user
    ) {
        var existing = organizationRepository
                .findByOrgId(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
        membersRepository
                .findByOrganizationAndUser(existing, user)
                .filter(m -> m.getRole() == ADMIN)
                .orElseThrow( () -> new AccessDeniedException("Operation not performed by an org. admin."));

        organizationRepository.delete(existing);
    }

}
