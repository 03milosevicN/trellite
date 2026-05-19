package org.example.trellite.board;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    Optional<List<Board>> findBoardByOrgId(Long orgId);
}
