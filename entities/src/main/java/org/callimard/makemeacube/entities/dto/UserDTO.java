package org.callimard.makemeacube.entities.dto;

import org.callimard.makemeacube.common.RegistrationProvider;
import org.callimard.makemeacube.entities.sql.User;

/**
 * Constructs a DTO of a {@link User}. The {@link User#getPassword()} field is not present for security reason.
 *
 * @param id
 * @param mail
 * @param pseudo
 * @param firstName
 * @param lastName
 * @param address
 * @param city
 * @param country
 * @param phone
 * @param isMaker
 * @param provider
 * @param creationDate
 */
public record UserDTO(int id, String mail, String pseudo, String firstName, String lastName, String address, String city,
                      String country, String phone, boolean isMaker, String makerDescription, RegistrationProvider provider, String creationDate) {

    public UserDTO(User user) {
        this(user.getId(), user.getMail(), user.getPseudo(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getCity(),
             user.getCountry(), user.getPhone(), user.getIsMaker(), user.getMakerDescription(), user.getProvider(),
             user.getCreationDate().toString());
    }
}
