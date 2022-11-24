package ratemypm.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ratemypm.domain.Question;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {}
