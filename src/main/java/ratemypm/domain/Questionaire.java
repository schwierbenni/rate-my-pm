package ratemypm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ratemypm.domain.enumeration.Gender;

/**
 * A Questionaire.
 */
@Entity
@Table(name = "questionaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Questionaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotNull
    @Column(name = "course", nullable = false)
    private String course;

    @NotNull
    @Column(name = "semester", nullable = false)
    private String semester;

    @NotNull
    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "total_rating")
    private Float totalRating;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "questionaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionaire" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "questionare")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionare" }, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questionaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Questionaire name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Questionaire description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Questionaire companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Questionaire gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return this.course;
    }

    public Questionaire course(String course) {
        this.setCourse(course);
        return this;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSemester() {
        return this.semester;
    }

    public Questionaire semester(String semester) {
        this.setSemester(semester);
        return this;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return this.department;
    }

    public Questionaire department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Float getTotalRating() {
        return this.totalRating;
    }

    public Questionaire totalRating(Float totalRating) {
        this.setTotalRating(totalRating);
        return this;
    }

    public void setTotalRating(Float totalRating) {
        this.totalRating = totalRating;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Questionaire user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setQuestionaire(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setQuestionaire(this));
        }
        this.questions = questions;
    }

    public Questionaire questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Questionaire addQuestion(Question question) {
        this.questions.add(question);
        question.setQuestionaire(this);
        return this;
    }

    public Questionaire removeQuestion(Question question) {
        this.questions.remove(question);
        question.setQuestionaire(null);
        return this;
    }

    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setQuestionare(null));
        }
        if (answers != null) {
            answers.forEach(i -> i.setQuestionare(this));
        }
        this.answers = answers;
    }

    public Questionaire answers(Set<Answer> answers) {
        this.setAnswers(answers);
        return this;
    }

    public Questionaire addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestionare(this);
        return this;
    }

    public Questionaire removeAnswer(Answer answer) {
        this.answers.remove(answer);
        answer.setQuestionare(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questionaire)) {
            return false;
        }
        return id != null && id.equals(((Questionaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questionaire{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", gender='" + getGender() + "'" +
            ", course='" + getCourse() + "'" +
            ", semester='" + getSemester() + "'" +
            ", department='" + getDepartment() + "'" +
            ", totalRating=" + getTotalRating() +
            "}";
    }
}
