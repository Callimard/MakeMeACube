package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import lombok.*;
import org.callimard.makemeacube.models.dto.DTOSerializable;
import org.callimard.makemeacube.models.dto.UserAddressDTO;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserAddress")
public class UserAddress implements DTOSerializable<UserAddressDTO> {

    // Constants.

    public static final String USER_ADDRESS_ID = "id";
    public static final String USER_ADDRESS_ADDRESS = "address";
    public static final String USER_ADDRESS_CITY = "city";
    public static final String USER_ADDRESS_COUNTRY = "country";
    public static final String USER_ADDRESS_POSTAL_CODE = "postalCode";

    public static final String USER_ADDRESS_USER = "user";

    // Variables.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_ADDRESS_ID, nullable = false)
    private Integer id;

    @Column(name = USER_ADDRESS_ADDRESS, nullable = false)
    private String address;

    @Column(name = USER_ADDRESS_CITY, nullable = false)
    private String city;

    @Column(name = USER_ADDRESS_COUNTRY, nullable = false)
    private String country;

    @Column(name = USER_ADDRESS_POSTAL_CODE, nullable = false)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = USER_ADDRESS_USER)
    private User user;

    // Methods.

    @Override
    public UserAddressDTO toDTO() {
        return new UserAddressDTO(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAddress that)) return false;
        return Objects.equal(address, that.address) && Objects.equal(city, that.city) &&
                Objects.equal(country, that.country) && Objects.equal(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address, city, country, postalCode);
    }
}
