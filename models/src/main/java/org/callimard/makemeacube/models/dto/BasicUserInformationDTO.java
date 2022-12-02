package org.callimard.makemeacube.models.dto;

import org.callimard.makemeacube.models.sql.User;

public record BasicUserInformationDTO(String mail,
                                      String pseudo,
                                      Boolean isMaker,
                                      String makerDescription,
                                      Float gradeAverage) {

    public BasicUserInformationDTO(User user) {
        this(user.getMail(),
             user.getPseudo(),
             user.getIsMaker(),
             user.getMakerDescription(),
             user.gradeAverage());
    }
}

