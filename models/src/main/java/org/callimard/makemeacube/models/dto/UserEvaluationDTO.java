package org.callimard.makemeacube.models.dto;

import org.callimard.makemeacube.models.sql.UserEvaluation;

public record UserEvaluationDTO(Integer id, Integer grade, String comment, String evaluatorPseudo, String evaluatedPseudo) {

    public UserEvaluationDTO(UserEvaluation userEvaluation) {
        this(userEvaluation.getId(), userEvaluation.getGrade(), userEvaluation.getComment(), userEvaluation.getEvaluator().getPseudo(),
             userEvaluation.getEvaluated().getPseudo());
    }
}
