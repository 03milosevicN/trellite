package org.example.trellite.org;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.common.RoleType;
import org.example.trellite.member.Member;
import org.example.trellite.member.MemberRepository;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.user.User;
import org.example.trellite.user.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final OrganizationMapper organizationMapper;


    // Query
    public OrganizationResponse getById(Long id) {
        return organizationRepository
                .findById(id)
                .map(organizationMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
    }

    public List<OrganizationResponse> getOwnedOrganizations(Long ownerId) {
        var ownedBy = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("User with id of + " + ownerId + "does not exist."));
        var orgs = organizationRepository.findOrganizationsByOwnedById(ownedBy).orElseThrow(() -> new RuntimeException("User owns no organizations."));
        return orgs
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
    }


    // Command
    @Transactional
    public OrganizationResponse save(OrganizationRequest req, User creator) {

        var model = organizationMapper.toModel(req);
        model.setCreatedAt(Instant.now());
        model.setOwnedBy(creator);
        var savedOrg = organizationRepository.save(model);

        var membership = new Member();
        membership.setOrganization(model);
        membership.setUser(creator);
        membership.setRole(RoleType.ADMIN);
        memberRepository.save(membership);

        log.info("User {} created new organization {} and received role of {}.", creator.getEmail(), savedOrg.getName(),RoleType.ADMIN);

        return organizationMapper.toResponse(savedOrg);
    }

    @Transactional
    public void joinOrg(Long orgId, User user) {

        var org = organizationRepository
                .findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + orgId + " not found."));

        if (memberRepository.existsByOrganizationIdAndUserId(orgId, user.getId())) {
            log.warn("User with ID of {} is already a member of {}", user.getId(), org.getName());
            throw new IllegalStateException();
        }

        var membership = new Member();
        membership.setOrganization(org);
        membership.setUser(user);
        membership.setRole(RoleType.MEMBER);
        memberRepository.save(membership);

        log.info("User {} joined {}.", user.getEmail(), org.getName());
    }


    @Transactional
    public void delete(Long id, User user) {

        var existing = organizationRepository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));

        memberRepository
                .findByOrganizationAndUser(existing, user)
                .filter(member -> member.getRole().equals(RoleType.ADMIN))
                .orElseThrow(() -> new AccessDeniedException("Unauthorized access. User is not an admin."));

        organizationRepository.delete(existing);
    }

}
