package ratemypm.web.rest;

import io.undertow.util.BadRequestException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ratemypm.domain.Questionaire;
import ratemypm.repository.QuestionaireRepository;
import ratemypm.security.SecurityUtils;
import ratemypm.service.QuestionaireService;
import ratemypm.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ratemypm.domain.Questionaire}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuestionaireResource {

    private final Logger log = LoggerFactory.getLogger(QuestionaireResource.class);

    private static final String ENTITY_NAME = "questionaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionaireRepository questionaireRepository;
    private final QuestionaireService questionaireService;

    public QuestionaireResource(QuestionaireRepository questionaireRepository, QuestionaireService questionaireService) {
        this.questionaireRepository = questionaireRepository;
        this.questionaireService = questionaireService;
    }

    /**
     * {@code POST  /questionaires} : Create a new questionaire.
     *
     * @param questionaire the questionaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionaire, or with status {@code 400 (Bad Request)} if the questionaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questionaires")
    public ResponseEntity<Questionaire> createQuestionaire(@Valid @RequestBody Questionaire questionaire)
        throws URISyntaxException, BadRequestException {
        log.debug("REST request to save Questionaire : {}", questionaire);
        if (questionaire.getId() != null) {
            throw new BadRequestAlertException("A new questionaire cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Questionaire result = questionaireService.save(questionaire);
        return ResponseEntity
            .created(new URI("/api/questionaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questionaires/:id} : Updates an existing questionaire.
     *
     * @param id           the id of the questionaire to save.
     * @param questionaire the questionaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionaire,
     * or with status {@code 400 (Bad Request)} if the questionaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questionaires/{id}")
    public ResponseEntity<Questionaire> updateQuestionaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Questionaire questionaire
    ) throws URISyntaxException {
        log.debug("REST request to update Questionaire : {}, {}", id, questionaire);
        if (questionaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Questionaire result = questionaireRepository.save(questionaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questionaires/:id} : Partial updates given fields of an existing questionaire, field will ignore if it is null
     *
     * @param id           the id of the questionaire to save.
     * @param questionaire the questionaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionaire,
     * or with status {@code 400 (Bad Request)} if the questionaire is not valid,
     * or with status {@code 404 (Not Found)} if the questionaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questionaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Questionaire> partialUpdateQuestionaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Questionaire questionaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Questionaire partially : {}, {}", id, questionaire);
        if (questionaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Questionaire> result = questionaireRepository
            .findById(questionaire.getId())
            .map(existingQuestionaire -> {
                if (questionaire.getName() != null) {
                    existingQuestionaire.setName(questionaire.getName());
                }
                if (questionaire.getDescription() != null) {
                    existingQuestionaire.setDescription(questionaire.getDescription());
                }
                if (questionaire.getCompanyName() != null) {
                    existingQuestionaire.setCompanyName(questionaire.getCompanyName());
                }
                if (questionaire.getGender() != null) {
                    existingQuestionaire.setGender(questionaire.getGender());
                }
                if (questionaire.getCourse() != null) {
                    existingQuestionaire.setCourse(questionaire.getCourse());
                }
                if (questionaire.getSemester() != null) {
                    existingQuestionaire.setSemester(questionaire.getSemester());
                }
                if (questionaire.getDepartment() != null) {
                    existingQuestionaire.setDepartment(questionaire.getDepartment());
                }
                if (questionaire.getTotalRating() != null) {
                    existingQuestionaire.setTotalRating(questionaire.getTotalRating());
                }

                return existingQuestionaire;
            })
            .map(questionaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionaire.getId().toString())
        );
    }

    /**
     * {@code GET  /questionaires} : get all the questionaires.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionaires in body.
     */
    @GetMapping("/questionaires")
    public ResponseEntity<List<Questionaire>> getAllQuestionaires(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Questionaires");

        Page<Questionaire> page;
        final Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();

        if (SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN")) {
            page = questionaireRepository.findAll(pageable);
        } else if (currentUserLogin.isPresent()) {
            page = questionaireRepository.findAllByUserLogin(currentUserLogin.get(), pageable);
        } else {
            page = Page.empty();
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questionaires/:id} : get the "id" questionaire.
     *
     * @param id the id of the questionaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questionaires/{id}")
    public ResponseEntity<Questionaire> getQuestionaire(@PathVariable Long id) {
        log.debug("REST request to get Questionaire : {}", id);
        Optional<Questionaire> questionaire = questionaireRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(questionaire);
    }

    /**
     * {@code DELETE  /questionaires/:id} : delete the "id" questionaire.
     *
     * @param id the id of the questionaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questionaires/{id}")
    public ResponseEntity<Void> deleteQuestionaire(@PathVariable Long id) {
        log.debug("REST request to delete Questionaire : {}", id);
        questionaireRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
