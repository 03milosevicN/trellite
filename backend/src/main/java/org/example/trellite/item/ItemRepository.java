package org.example.trellite.item;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByChecklistId(String checklistId);
    void deleteItemByChecklistId(String checklistId);
}
