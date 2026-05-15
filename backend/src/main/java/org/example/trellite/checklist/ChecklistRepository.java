package org.example.trellite.checklist;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String> {
    List<Checklist> findByCardId(String cardId);
}
