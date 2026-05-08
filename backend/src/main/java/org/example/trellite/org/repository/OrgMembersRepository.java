package org.example.trellite.org.repository;

import org.example.trellite.org.model.OrgMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrgMembersRepository extends JpaRepository<OrgMembers, Long> {

    Optional<OrgMembers> findByOrgMembersId(Long id);

}
