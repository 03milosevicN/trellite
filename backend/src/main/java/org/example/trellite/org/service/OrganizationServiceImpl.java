package org.example.trellite.org.service;

import lombok.RequiredArgsConstructor;
import org.example.trellite.common.BaseService;
import org.example.trellite.common.ResourceNotFoundException;
import org.example.trellite.org.dto.NameUpdateRequest;
import org.example.trellite.org.dto.OrganizationRequest;
import org.example.trellite.org.dto.OrganizationResponse;
import org.example.trellite.org.mapper.OrganizationMapper;
import org.example.trellite.org.repository.OrganizationRepository;
import org.example.trellite.user.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements BaseService<OrganizationRequest, OrganizationResponse, Long> {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMapper organizationMapper;


    @Override
    public List<OrganizationResponse> getAll() {
        return organizationRepository
                .findAll()
                .stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationResponse getById(Long id) {
        return organizationRepository
                .findByOrgId(id)
                .map(organizationMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
    }

    @Override
    public OrganizationResponse save(OrganizationRequest dto) {
        var model = organizationMapper.toModel(dto);
        var user = userRepository.getReferenceById(dto.getOwnedBy());
        model.setOwnedBy(user);
        var saved = organizationRepository.save(model);
        return organizationMapper.toResponse(saved);
    }

    @Override
    public OrganizationResponse update(Long id, OrganizationRequest dto) {
        var existing = organizationRepository
                .findByOrgId(id)
                .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."));
        var user = userRepository
                .findByUserId(dto.getOwnedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + dto.getOwnedBy() + " not found."));
        existing.setName(dto.getName());
        existing.setCreatedAt(Instant.now());
        existing.setOwnedBy(user);
        return organizationMapper.toResponse(organizationRepository.save(existing));
    }

    public OrganizationResponse patchName(Long orgId, NameUpdateRequest req) {
        var existing = organizationRepository.findByOrgId(orgId).orElseThrow(() -> new ResourceNotFoundException("Organization with id of " + orgId + " not found."));
        if (req.getName() != null) existing.setName(req.getName());
        return organizationMapper.toResponse(organizationRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        organizationRepository.delete(
                organizationRepository
                        .findByOrgId(id)
                        .orElseThrow( () -> new ResourceNotFoundException("Organization with id of " + id + " not found."))
        );
    }



}
