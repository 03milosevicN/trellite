package org.example.trellite.card;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
    void deleteAllByBoardListId(ObjectId boardListId);
    List<Card> findByBoardListId(ObjectId boardListId);
    List<Card> findByBoardIdAndBoardListIdIsNull(ObjectId boardId);
    // For backlog + board-list fetching
    List<Card> findByAssigneesContaining(Long userId);
    // For backlog fetching
    List<Card> findByAssigneesContainingAndBoardListIdIsNull(Long userId);
}
