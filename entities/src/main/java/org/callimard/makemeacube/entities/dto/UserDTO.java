package org.callimard.makemeacube.entities.dto;

import org.callimard.makemeacube.common.RegistrationProvider;
import org.callimard.makemeacube.entities.sql.User;

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
 * @param provider
 * @param creationDate
 */
public record UserDTO(int id, String mail, String pseudo, String firstName, String lastName, List<UserAddressDTO> addresses, String phone,
                      boolean isMaker, String makerDescription, RegistrationProvider provider, String creationDate) {

    public UserDTO(User user) {
        this(user.getId(), user.getMail(), user.getPseudo(), user.getFirstName(), user.getLastName(),
             user.getAddresses().stream().map(UserAddressDTO::new).toList(), user.getPhone(), user.getIsMaker(), user.getMakerDescription(),
             user.getProvider(), user.getCreationDate().toString());
    }
}
