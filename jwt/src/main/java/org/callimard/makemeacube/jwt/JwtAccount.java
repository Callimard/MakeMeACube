package org.callimard.makemeacube.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAccount {

    // Constructors.

    public static final String CLAIM_ACCOUNT = "account";

    public static final String CLAIM_USER_ID = "id";
    public static final String CLAIM_MAIL = "mail";
    public static final String CLAIM_PSEUDO = "pseudo";
    public static final String CLAIM_FIRST_NAME = "firstName";
    public static final String CLAIM_LAST_NAME = "lastName";

    // Variables.

    private Integer id;
    private String mail;
    private String pseudo;
    private String firstName;
    private String lastName;
    private List<String> privileges;

    // Methods.

    public static JwtAccount extractAccountFrom(ObjectMapper mapper, DecodedJWT jwt) throws JsonProcessingException {
        return mapper.readValue(jwt.getClaim(CLAIM_ACCOUNT).asString(), JwtAccount.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtAccount that)) return false;
        return Objects.equal(id, that.id) && Objects.equal(mail, that.mail) && Objects.equal(pseudo, that.pseudo) &&
                Objects.equal(firstName, that.firstName) && Objects.equal(lastName, that.lastName) &&
                Objects.equal(privileges, that.privileges);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, mail, pseudo, firstName, lastName, privileges);
    }
}
