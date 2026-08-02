package org.example.trellite.chat;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, ObjectId> {
    //! JPA not tested.
    Page<ChatMessage> findByBoardIdOrderBySentAtDesc(String boardId, Pageable pageable);
}
