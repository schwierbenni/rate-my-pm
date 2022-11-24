package ratemypm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ratemypm.IntegrationTest;
import ratemypm.domain.Questionaire;
import ratemypm.domain.enumeration.Gender;
import ratemypm.repository.QuestionaireRepository;
import ratemypm.security.AuthoritiesConstants;

/**
 * Integration tests for the {@link QuestionaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionaireResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_COURSE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE = "BBBBBBBBBB";

    private static final String DEFAULT_SEMESTER = "AAAAAAAAAA";
    private static final String UPDATED_SEMESTER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final Float DEFAULT_TOTAL_RATING = 1F;
    private static final Float UPDATED_TOTAL_RATING = 2F;

    private static final String ENTITY_API_URL = "/api/questionaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionaireRepository questionaireRepository;

    @Mock
    private QuestionaireRepository questionaireRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionaireMockMvc;

    private Questionaire questionaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionaire createEntity(EntityManager em) {
        Questionaire questionaire = new Questionaire()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .companyName(DEFAULT_COMPANY_NAME)
            .gender(DEFAULT_GENDER)
            .course(DEFAULT_COURSE)
            .semester(DEFAULT_SEMESTER)
            .department(DEFAULT_DEPARTMENT)
            .totalRating(DEFAULT_TOTAL_RATING);
        return questionaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionaire createUpdatedEntity(EntityManager em) {
        Questionaire questionaire = new Questionaire()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyName(UPDATED_COMPANY_NAME)
            .gender(UPDATED_GENDER)
            .course(UPDATED_COURSE)
            .semester(UPDATED_SEMESTER)
            .department(UPDATED_DEPARTMENT)
            .totalRating(UPDATED_TOTAL_RATING);
        return questionaire;
    }

    @BeforeEach
    public void initTest() {
        questionaire = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionaire() throws Exception {
        int databaseSizeBeforeCreate = questionaireRepository.findAll().size();
        // Create the Questionaire
        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isCreated());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeCreate + 1);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuestionaire.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testQuestionaire.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testQuestionaire.getCourse()).isEqualTo(DEFAULT_COURSE);
        assertThat(testQuestionaire.getSemester()).isEqualTo(DEFAULT_SEMESTER);
        assertThat(testQuestionaire.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testQuestionaire.getTotalRating()).isEqualTo(DEFAULT_TOTAL_RATING);
    }

    @Test
    @Transactional
    void createQuestionaireWithExistingId() throws Exception {
        // Create the Questionaire with an existing ID
        questionaire.setId(1L);

        int databaseSizeBeforeCreate = questionaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionaireRepository.findAll().size();
        // set the field null
        questionaire.setName(null);

        // Create the Questionaire, which fails.

        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isBadRequest());

        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionaireRepository.findAll().size();
        // set the field null
        questionaire.setCompanyName(null);

        // Create the Questionaire, which fails.

        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isBadRequest());

        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionaireRepository.findAll().size();
        // set the field null
        questionaire.setCourse(null);

        // Create the Questionaire, which fails.

        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isBadRequest());

        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSemesterIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionaireRepository.findAll().size();
        // set the field null
        questionaire.setSemester(null);

        // Create the Questionaire, which fails.

        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isBadRequest());

        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionaireRepository.findAll().size();
        // set the field null
        questionaire.setDepartment(null);

        // Create the Questionaire, which fails.

        restQuestionaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isBadRequest());

        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuestionaires() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        // Get all the questionaireList
        restQuestionaireMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isOk());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionaireRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionaireRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionaireRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(questionaireRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuestionaire() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        // Get the questionaire
        restQuestionaireMockMvc
            .perform(get(ENTITY_API_URL_ID, questionaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionaire.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.course").value(DEFAULT_COURSE))
            .andExpect(jsonPath("$.semester").value(DEFAULT_SEMESTER))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.totalRating").value(DEFAULT_TOTAL_RATING.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuestionaire() throws Exception {
        // Get the questionaire
        restQuestionaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionaire() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();

        // Update the questionaire
        Questionaire updatedQuestionaire = questionaireRepository.findById(questionaire.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionaire are not directly saved in db
        em.detach(updatedQuestionaire);
        updatedQuestionaire
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyName(UPDATED_COMPANY_NAME)
            .gender(UPDATED_GENDER)
            .course(UPDATED_COURSE)
            .semester(UPDATED_SEMESTER)
            .department(UPDATED_DEPARTMENT)
            .totalRating(UPDATED_TOTAL_RATING);

        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestionaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestionaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuestionaire.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testQuestionaire.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testQuestionaire.getCourse()).isEqualTo(UPDATED_COURSE);
        assertThat(testQuestionaire.getSemester()).isEqualTo(UPDATED_SEMESTER);
        assertThat(testQuestionaire.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testQuestionaire.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
    }

    @Test
    @Transactional
    void putNonExistingQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionaireWithPatch() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();

        // Update the questionaire using partial update
        Questionaire partialUpdatedQuestionaire = new Questionaire();
        partialUpdatedQuestionaire.setId(questionaire.getId());

        partialUpdatedQuestionaire
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyName(UPDATED_COMPANY_NAME)
            .semester(UPDATED_SEMESTER)
            .department(UPDATED_DEPARTMENT)
            .totalRating(UPDATED_TOTAL_RATING);

        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuestionaire.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testQuestionaire.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testQuestionaire.getCourse()).isEqualTo(DEFAULT_COURSE);
        assertThat(testQuestionaire.getSemester()).isEqualTo(UPDATED_SEMESTER);
        assertThat(testQuestionaire.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testQuestionaire.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
    }

    @Test
    @Transactional
    void fullUpdateQuestionaireWithPatch() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();

        // Update the questionaire using partial update
        Questionaire partialUpdatedQuestionaire = new Questionaire();
        partialUpdatedQuestionaire.setId(questionaire.getId());

        partialUpdatedQuestionaire
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .companyName(UPDATED_COMPANY_NAME)
            .gender(UPDATED_GENDER)
            .course(UPDATED_COURSE)
            .semester(UPDATED_SEMESTER)
            .department(UPDATED_DEPARTMENT)
            .totalRating(UPDATED_TOTAL_RATING);

        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuestionaire.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testQuestionaire.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testQuestionaire.getCourse()).isEqualTo(UPDATED_COURSE);
        assertThat(testQuestionaire.getSemester()).isEqualTo(UPDATED_SEMESTER);
        assertThat(testQuestionaire.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testQuestionaire.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionaire() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeDelete = questionaireRepository.findAll().size();

        // Delete the questionaire
        restQuestionaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
