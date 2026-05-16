package org.example.trellite.checklist;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {
    Optional<List<Checklist>> findByCardId(String cardId);
}
