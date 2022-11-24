package ratemypm.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ratemypm.domain.Questionaire;

/**
 * Spring Data JPA repository for the Questionaire entity.
 */
@Repository
public interface QuestionaireRepository extends JpaRepository<Questionaire, Long> {
    default Optional<Questionaire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Questionaire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Questionaire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct questionaire from Questionaire questionaire left join fetch questionaire.user",
        countQuery = "select count(distinct questionaire) from Questionaire questionaire"
    )
    Page<Questionaire> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct questionaire from Questionaire questionaire left join fetch questionaire.user")
    List<Questionaire> findAllWithToOneRelationships();

    @Query("select questionaire from Questionaire questionaire left join fetch questionaire.user where questionaire.id =:id")
    Optional<Questionaire> findOneWithToOneRelationships(@Param("id") Long id);

    Page<Questionaire> findAllByUserLogin(String username, Pageable pageable);
}
