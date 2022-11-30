package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import lombok.*;
import org.callimard.makemeacube.models.dto.DTOSerializable;
import org.callimard.makemeacube.models.dto.UserEvaluationDTO;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserEvaluation")
public class UserEvaluation implements DTOSerializable<UserEvaluationDTO> {

    // Constants.

    public static final String USER_EVALUATION_ID = "id";
    public static final String USER_EVALUATION_GRADE = "grade";
    public static final String USER_EVALUATION_COMMENT = "comment";
    public static final String USER_EVALUATION_EVALUATOR = "evaluator";
    public static final String USER_EVALUATION_EVALUATED = "evaluated";

    // Variables.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_EVALUATION_ID, nullable = false)
    private Integer id;

    @Column(name = USER_EVALUATION_GRADE, nullable = false)
    private Integer grade;

    @Column(name = USER_EVALUATION_COMMENT)
    private String comment;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_EVALUATION_EVALUATOR)
    private User evaluator;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_EVALUATION_EVALUATED)
    private User evaluated;

    // Methods.

    @Override
    public UserEvaluationDTO toDTO() {
        return new UserEvaluationDTO(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEvaluation userEvaluation)) return false;
        return Objects.equal(grade, userEvaluation.grade) && Objects.equal(comment, userEvaluation.comment) && Objects.equal(evaluator, userEvaluation.evaluator) &&
                Objects.equal(evaluated, userEvaluation.evaluated);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(grade, comment, evaluator, evaluated);
    }
}
