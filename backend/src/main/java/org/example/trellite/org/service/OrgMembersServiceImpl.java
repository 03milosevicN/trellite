package org.example.trellite.org.service;

import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.org.dto.OrgMembersRequest;
import org.example.trellite.org.dto.OrgMembersResponse;
import org.example.trellite.org.mapper.OrgMembersMapper;
import org.example.trellite.org.repository.OrgMembersRepository;
import org.example.trellite.org.repository.OrganizationRepository;
import org.example.trellite.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgMembersServiceImpl implements BaseService<OrgMembersRequest, OrgMembersResponse, Long> {

    private final OrgMembersRepository orgMembersRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrgMembersMapper orgMembersMapper;

    @Autowired
    public OrgMembersServiceImpl(
            OrgMembersRepository orgMembersRepository,
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            OrgMembersMapper orgMembersMapper
    ) {
        this.orgMembersRepository = orgMembersRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.orgMembersMapper = orgMembersMapper;
    }


    @Override
    public List<OrgMembersResponse> getAll() {
        return orgMembersRepository
                .findAll()
                .stream()
                .map(orgMembersMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrgMembersResponse getById(Long id) {
        return orgMembersRepository
                .findByOrgMembersId(id)
                .map(orgMembersMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + id + " not found."));
    }

    @Override
    public OrgMembersResponse save(OrgMembersRequest dto) {
        var model = orgMembersMapper.toModel(dto);
        var saved = orgMembersRepository.save(model);
        return orgMembersMapper.toResponse(saved);
    }

    @Override
    public OrgMembersResponse update(Long id, OrgMembersRequest dto) {
        var existing = orgMembersRepository
                .findByOrgMembersId(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + id + " not found."));
        var user = userRepository
                .findByUserId(dto.getUserId())
                .orElseThrow( () -> new ResourceNotFoundException("User with id of " + dto.getUserId() + " not found."));
        var org = organizationRepository
                .findByOrgId(dto.getOrgId())
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + dto.getOrgId() + " not found."));
        existing.setUser(user);
        existing.setOrganization(org);
        existing.setRole(dto.getRole());
        return orgMembersMapper.toResponse(orgMembersRepository.save(existing));
    }

    @Override
    public OrgMembersResponse patch(Long id, OrgMembersRequest dto) {
        var existing = orgMembersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization member with id of " + id + " not found."));
        if (dto.getRole() != null) dto.setRole(dto.getRole());
        return orgMembersMapper.toResponse(orgMembersRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        orgMembersRepository.delete(
                orgMembersRepository
                        .findByOrgMembersId(id)
                        .orElseThrow( () -> new ResourceNotFoundException("Organization member entry with id of " + id + " not found.") )
        );
    }

}
