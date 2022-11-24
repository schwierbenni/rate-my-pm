package ratemypm.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ratemypm.domain.Answer;

/**
 * Spring Data JPA repository for the Answer entity.
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    default Optional<Answer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Answer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Answer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct answer from Answer answer left join fetch answer.questionare",
        countQuery = "select count(distinct answer) from Answer answer"
    )
    Page<Answer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct answer from Answer answer left join fetch answer.questionare")
    List<Answer> findAllWithToOneRelationships();

    @Query("select answer from Answer answer left join fetch answer.questionare where answer.id =:id")
    Optional<Answer> findOneWithToOneRelationships(@Param("id") Long id);

    List<Answer> findByQuestionareUserLogin(String login);

    Page<Answer> findAllByQuestionareUserLogin(String s, Pageable pageable);
}
