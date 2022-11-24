package ratemypm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ratemypm.web.rest.TestUtil;

class QuestionaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questionaire.class);
        Questionaire questionaire1 = new Questionaire();
        questionaire1.setId(1L);
        Questionaire questionaire2 = new Questionaire();
        questionaire2.setId(questionaire1.getId());
        assertThat(questionaire1).isEqualTo(questionaire2);
        questionaire2.setId(2L);
        assertThat(questionaire1).isNotEqualTo(questionaire2);
        questionaire1.setId(null);
        assertThat(questionaire1).isNotEqualTo(questionaire2);
    }
}
