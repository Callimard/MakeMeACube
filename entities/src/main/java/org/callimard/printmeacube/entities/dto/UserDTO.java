package org.callimard.printmeacube.entities.dto;

import org.callimard.printmeacube.common.RegistrationProvider;
import org.callimard.printmeacube.entities.sql.User;

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
 * @param isPrinter
 * @param provider
 * @param creationDate
 */
public record UserDTO(int id, String mail, String pseudo, String firstName, String lastName, String address, String city,
                      String country, String phone, boolean isPrinter, RegistrationProvider provider, String creationDate) {

    public UserDTO(User user) {
        this(user.getId(), user.getMail(), user.getPseudo(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getCity(),
             user.getCountry(), user.getPhone(), user.getIsPrinter(), user.getProvider(), user.getCreationDate().toString());
    }
}
