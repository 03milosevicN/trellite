package org.example.trellite.org;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @NonNull Optional<Organization> findById(Long id);

}
