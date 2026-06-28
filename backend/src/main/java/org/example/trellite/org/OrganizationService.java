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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;
    private final OrganizationMapper organizationMapper;


    public OrganizationResponse getById(Long id) {
        return organizationRepository
                .findById(id)
                .map(organizationMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
    }


    @Transactional
    public OrganizationResponse save(OrganizationRequest req, User creator) {

        var model = organizationMapper.toModel(req);
        model.setCreatedAt(Instant.now());
        var saved = organizationRepository.save(model);

        var membership = new Member();
        membership.setOrganization(model);
        membership.setUser(creator);
        membership.setRole(RoleType.ADMIN);
        memberRepository.save(membership);

        log.info("User {} created new organization and received role of {}.", creator.getEmail(), RoleType.ADMIN);

        return organizationMapper.toResponse(saved);
    }

    @Transactional
    public void joinOrg(Long orgId, User user) {

        var org = organizationRepository
                .findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + orgId + " not found."));



    }


    @Transactional
    public void delete(
            Long id,
            User user
    ) {
        var roleByName = roleRepository.findByName("ADMIN").orElseThrow(() -> new NullPointerException("Role with name of ADMIN is null (does not exist)."));

        var existing = organizationRepository
                .findByOrgId(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
        membersRepository
                .findByOrganizationAndUser(existing, user)
                .filter(m -> m.getRoles().contains(roleByName))
                .orElseThrow( () -> new AccessDeniedException("Operation not performed by an org. admin."));

        organizationRepository.delete(existing);
    }

}
