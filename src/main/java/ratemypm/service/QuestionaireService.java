package ratemypm.service;

import io.undertow.util.BadRequestException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ratemypm.domain.Answer;
import ratemypm.domain.Question;
import ratemypm.domain.Questionaire;
import ratemypm.domain.User;
import ratemypm.repository.AnswerRepository;
import ratemypm.repository.QuestionRepository;
import ratemypm.repository.QuestionaireRepository;
import ratemypm.security.SecurityUtils;

/**
 * Service Implementation for managing Connection.
 */
@Service
@Transactional
public class QuestionaireService {

    @Autowired
    private QuestionaireRepository questionaireRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserService userService;

    public Questionaire save(Questionaire questionaire) throws BadRequestException {
        if (questionaire.getUser() == null) {
            questionaire.setUser(fetchCurrentUserInformation());
        }
        final List<Question> allQuestions = questionRepository.findAll();
        final Questionaire savedQuestionaire = questionaireRepository.save(questionaire);

        if (
            questionaire.getAnswers().isEmpty() &&
            answerRepository.findByQuestionareUserLogin(Objects.requireNonNull(fetchCurrentUserInformation()).getLogin()).isEmpty()
        ) {
            allQuestions
                .stream()
                .forEach(question -> answerRepository.save(new Answer().questionare(questionaire).questionText(question.getQuestionText()))
                );
        }

        return savedQuestionaire;
    }

    private User fetchCurrentUserInformation() {
        final Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isPresent()) {
            final Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(
                currentUserLogin.get().toLowerCase()
            );
            if (userWithAuthoritiesByLogin.isPresent()) {
                return userWithAuthoritiesByLogin.get();
            }
        }
        return null;
    }
}
