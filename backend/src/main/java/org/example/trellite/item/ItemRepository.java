package org.example.trellite.item;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    void deleteItemByChecklistId(String id);
    List<Item> findByChecklistId(String checklistId);
}
