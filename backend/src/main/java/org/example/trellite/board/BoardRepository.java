package org.example.trellite.board;

import org.bson.types.ObjectId;
import org.example.trellite.board.projections.BoardIdProjection;
import org.example.trellite.board.projections.BoardMembersProjection;
import org.example.trellite.member.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    Optional<List<Board>> findBoardsByOrgId(Long orgId);

    @Query("{ 'org_id': ?0 }")
    List<BoardMembersProjection> projectFindBoardsByOrgId(Long orgId);
    @Query("{'org_id': ?0, 'members': ?1 }")
    List<BoardMembersProjection> projectFindByOrgIdAndMembersContaining(Long orgId, Long userId);

    @Query("{ 'org_id': ?0, 'members': ?1 }")
    List<BoardIdProjection> findBoardIdsByOrgIdAndMember(Long orgId, Long userId);

    Optional<Board> findBoardByOrgId(Long orgId);

    Optional<Board> findById(ObjectId boardId);

    boolean existsByIdAndMembersContains(ObjectId boardId, Long userId);

    List<Board> id(ObjectId id);
}
