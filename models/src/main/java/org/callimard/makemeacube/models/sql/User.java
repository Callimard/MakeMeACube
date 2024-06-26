package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.*;
import org.callimard.makemeacube.models.aop.MakerToolId;
import org.callimard.makemeacube.models.aop.UserAddressId;
import org.callimard.makemeacube.models.aop.UserEvaluationId;
import org.callimard.makemeacube.models.dto.DTOSerializable;
import org.callimard.makemeacube.models.dto.UserDTO;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    public static final String USER_PHONE = "phone";
    public static final String USER_IS_MAKER = "isMaker";
    public static final String USER_TOOLS = "tools";
    public static final String USER_MAKER_DESCRIPTION = "makerDescription";
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

    @ToString.Exclude
    @OneToMany(targetEntity = UserAddress.class, mappedBy = UserAddress.USER_ADDRESS_USER)
    private List<UserAddress> addresses = Lists.newArrayList();

    @Column(name = USER_PHONE)
    private String phone;

    @Column(name = USER_IS_MAKER, nullable = false)
    private Boolean isMaker;

    @Column(name = USER_MAKER_DESCRIPTION)
    private String makerDescription;

    @ToString.Exclude
    @OneToMany(targetEntity = MakerTool.class, mappedBy = MakerTool.MAKER_TOOL_OWNER)
    private List<MakerTool> tools = Lists.newArrayList();

    @ToString.Exclude
    @OneToMany(targetEntity = UserEvaluation.class, mappedBy = UserEvaluation.USER_EVALUATION_EVALUATOR)
    private List<UserEvaluation> evaluations = Lists.newArrayList();

    @ToString.Exclude
    @OneToMany(targetEntity = UserEvaluation.class, mappedBy = UserEvaluation.USER_EVALUATION_EVALUATED)
    private List<UserEvaluation> grades = Lists.newArrayList();

    @Column(name = USER_PROVIDER, nullable = false)
    private RegistrationProvider provider;

    @Column(name = USER_CREATION_DATE, nullable = false)
    private Instant creationDate;

    // Constructors.

    public User(@NonNull String mail, @NonNull String pseudo, @NonNull String password, RegistrationProvider provider, Instant creationDate) {
        this(-1, mail, pseudo, password, null, null, Lists.newArrayList(), null, false, null, Lists.newArrayList(), Lists.newArrayList(),
             Lists.newArrayList(), provider, creationDate);
    }

    public User(User user) {
        this.id = user.id;
        this.mail = user.mail;
        this.pseudo = user.pseudo;
        this.password = user.password;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.addresses = Lists.newArrayList(user.getAddresses());
        this.phone = user.phone;
        this.isMaker = user.isMaker;
        this.makerDescription = user.makerDescription;
        this.tools = Lists.newArrayList(user.tools);
        this.evaluations = Lists.newArrayList(user.evaluations);
        this.grades = Lists.newArrayList(user.grades);
        this.provider = user.provider;
        this.creationDate = user.creationDate;
    }

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
                Objects.equal(lastName, user.lastName) && Objects.equal(addresses, user.addresses) &&
                Objects.equal(phone, user.phone) && Objects.equal(isMaker, user.isMaker) &&
                Objects.equal(makerDescription, user.makerDescription) && Objects.equal(tools, user.tools);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mail, pseudo, password, firstName, lastName, addresses, phone, isMaker, makerDescription, tools);
    }

    public float gradeAverage() {
        if (grades.isEmpty()) {
            return -1.0f;
        }

        int sum = grades.stream().map(UserEvaluation::getGrade).reduce(Integer::sum).orElse(0);
        return (float) sum / (float) grades.size();
    }

    public Optional<UserAddress> getUserAddressWith(@NonNull @UserAddressId Integer userAddressId) {
        for (UserAddress userAddress : addresses) {
            if (userAddress.getId().equals(userAddressId)) {
                return Optional.of(userAddress);
            }
        }
        return Optional.empty();
    }

    public void removeUserAddressWith(@NonNull @UserAddressId Integer userAddressId) {
        addresses.removeIf(userAddress -> userAddress.getId().equals(userAddressId));
    }

    public Optional<MakerTool> getMakerToolWith(@NonNull @MakerToolId Integer makerToolId) {
        for (MakerTool makerTool : tools) {
            if (makerTool.getId().equals(makerToolId)) {
                return Optional.of(makerTool);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getMakerTool(@NonNull @MakerToolId Integer makerToolId, Class<T> makerToolClass) {
        var makerTool = getMakerToolWith(makerToolId);
        if (makerTool.isPresent() && makerToolClass.isInstance(makerTool.get())) {
            return Optional.of((T) makerTool.get());
        }

        return Optional.empty();
    }

    public Optional<UserEvaluation> getUserEvaluations(@NonNull @UserEvaluationId Integer userEvaluationId) {
        for (UserEvaluation userEvaluation : evaluations) {
            if (userEvaluation.getId().equals(userEvaluationId)) {
                return Optional.of(userEvaluation);
            }
        }

        return Optional.empty();
    }

    public void removeUserEvaluation(@NonNull @UserEvaluationId Integer userEvaluationId) {
        evaluations.removeIf(userEvaluation -> userEvaluation.getId().equals(userEvaluationId));
    }

    public void removeMakerToolWith(@NonNull @MakerToolId Integer makerToolId) {
        tools.removeIf(makerTool -> makerTool.getId().equals(makerToolId));
    }
}
