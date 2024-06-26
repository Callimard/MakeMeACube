package org.callimard.makemeacube.models.dto;

import org.callimard.makemeacube.models.sql.MakerTool;
import org.callimard.makemeacube.models.sql.RegistrationProvider;
import org.callimard.makemeacube.models.sql.User;
import org.callimard.makemeacube.models.sql.UserEvaluation;

import java.util.List;

/**
 * Constructs a DTO of a {@link User}. The {@link User#getPassword()} field is not present for security reason.
 *
 * @param id
 * @param mail
 * @param pseudo
 * @param firstName
 * @param lastName
 * @param addresses
 * @param phone
 * @param isMaker
 * @param makerDescription
 * @param tools
 * @param evaluations
 * @param grades
 * @param provider
 * @param creationDate
 */
public record UserDTO(int id, String mail, String pseudo, String firstName, String lastName, List<UserAddressDTO> addresses, String phone,
                      boolean isMaker, String makerDescription,
                      List<MakerToolDTO> tools,
                      List<UserEvaluationDTO> evaluations,
                      List<UserEvaluationDTO> grades,
                      RegistrationProvider provider,
                      String creationDate) {

    public UserDTO(User user) {
        this(user.getId(), user.getMail(), user.getPseudo(), user.getFirstName(), user.getLastName(),
             user.getAddresses().stream().map(UserAddressDTO::new).toList(),
             user.getPhone(), user.getIsMaker(),
             user.getMakerDescription(),
             user.getTools().stream().map(MakerTool::toDTO).toList(),
             user.getEvaluations().stream().map(UserEvaluation::toDTO).toList(),
             user.getGrades().stream().map(UserEvaluation::toDTO).toList(),
             user.getProvider(), user.getCreationDate().toString());
    }
}
