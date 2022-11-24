package ratemypm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Answer.
 */
@Entity
@Table(name = "answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "optional_message")
    private String optionalMessage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questions", "answers" }, allowSetters = true)
    private Questionaire questionare;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Answer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public Answer questionText(String questionText) {
        this.setQuestionText(questionText);
        return this;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Float getRating() {
        return this.rating;
    }

    public Answer rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getOptionalMessage() {
        return this.optionalMessage;
    }

    public Answer optionalMessage(String optionalMessage) {
        this.setOptionalMessage(optionalMessage);
        return this;
    }

    public void setOptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }

    public Questionaire getQuestionare() {
        return this.questionare;
    }

    public void setQuestionare(Questionaire questionaire) {
        this.questionare = questionaire;
    }

    public Answer questionare(Questionaire questionaire) {
        this.setQuestionare(questionaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return id != null && id.equals(((Answer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", questionText='" + getQuestionText() + "'" +
            ", rating=" + getRating() +
            ", optionalMessage='" + getOptionalMessage() + "'" +
            "}";
    }
}
