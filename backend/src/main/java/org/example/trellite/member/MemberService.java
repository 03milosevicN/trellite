package org.example.trellite.member;

import lombok.RequiredArgsConstructor;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.common.RoleType;
import org.example.trellite.member.dto.MemberResponse;
import org.example.trellite.org.OrganizationRepository;
import org.example.trellite.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberMapper memberMapper;


    public MemberResponse getById(Long id) {
        return memberRepository
                .findById(id)
                .map(memberMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Member with id of " + id + " not found."));
    }

    @Transactional
    public void leaveOrg(Long orgId, User member) {

        var existing = organizationRepository
                .findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + orgId + " not found."));

        memberRepository
                .findByOrganizationAndUser(existing, member)
                .filter(m -> m.getRole().equals(RoleType.MEMBER) || m.getRole().equals(RoleType.ADMIN))
                .orElseThrow(() -> new AccessDeniedException("Unauthorized access. User lacks either of the possible roles."));

        memberRepository.deleteByOrganizationIdAndUserId(orgId, member.getId());
    }

}
