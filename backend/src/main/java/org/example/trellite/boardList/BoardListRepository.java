package org.example.trellite.boardList;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoardListRepository extends MongoRepository<BoardList, String> {
    List<BoardList> findByBoardId(String boardId);
}
