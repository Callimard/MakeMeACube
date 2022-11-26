package org.callimard.printmeacube.entities.sql;

import com.google.common.base.Objects;
import lombok.*;
import org.callimard.printmeacube.common.RegistrationProvider;
import org.callimard.printmeacube.entities.dto.DTOSerializable;
import org.callimard.printmeacube.entities.dto.UserDTO;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User implements DTOSerializable<UserDTO> {

    // Constants.

    public static final String USER_ID = "id";
    public static final String USER_MAIL = "mail";
    public static final String USER_PSEUDO = "pseudo";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_ADDRESS = "address";
    public static final String USER_CITY = "city";
    public static final String USER_COUNTRY = "country";
    public static final String USER_PHONE = "phone";
    public static final String USER_IS_PRINTER = "isPrinter";
    public static final String USER_PROVIDER = "provider";
    public static final String USER_CREATION_DATE = "creationDate";

    // Variables.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_ID, nullable = false)
    private Integer id;

    @Column(name = USER_MAIL, nullable = false, unique = true)
    private String mail;

    @Column(name = USER_PSEUDO, nullable = false, unique = true)
    private String pseudo;

    @Column(name = USER_PASSWORD, nullable = false)
    private String password;

    @Column(name = USER_FIRST_NAME)
    private String firstName;

    @Column(name = USER_LAST_NAME)
    private String lastName;

    @Column(name = USER_ADDRESS)
    private String address;

    @Column(name = USER_CITY)
    private String city;

    @Column(name = USER_COUNTRY)
    private String country;

    @Column(name = USER_PHONE)
    private String phone;

    @Column(name = USER_IS_PRINTER, nullable = false)
    private Boolean isPrinter;

    @Column(name = USER_PROVIDER, nullable = false)
    private RegistrationProvider provider;

    @Column(name = USER_CREATION_DATE, nullable = false)
    private Instant creationDate;

    // Methods.

    @Override
    public UserDTO toDTO() {
        return new UserDTO(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equal(mail, user.mail) && Objects.equal(pseudo, user.pseudo) &&
                Objects.equal(password, user.password) && Objects.equal(firstName, user.firstName) &&
                Objects.equal(lastName, user.lastName) && Objects.equal(address, user.address) &&
                Objects.equal(city, user.city) && Objects.equal(country, user.country) &&
                Objects.equal(phone, user.phone) && Objects.equal(isPrinter, user.isPrinter);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mail, pseudo, password, firstName, lastName, address, city, country, phone, isPrinter);
    }
}
