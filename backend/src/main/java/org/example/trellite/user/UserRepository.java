package org.example.trellite.user;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull Optional<User> findById(Long id);
    @NonNull Optional<User> findByEmail(String email);

}
