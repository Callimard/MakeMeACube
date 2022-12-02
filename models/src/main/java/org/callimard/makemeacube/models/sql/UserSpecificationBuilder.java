package org.callimard.makemeacube.models.sql;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class UserSpecificationBuilder {

    // Variables.

    private final List<Specification<User>> userBasicSpecifications = Lists.newArrayList();
    private final List<Specification<User>> userMaterialSpecifications = Lists.newArrayList();
    private final List<Specification<User>> userMaterialColorSpecifications = Lists.newArrayList();
    private final List<Specification<User>> userPrinter3DTypeSpecifications = Lists.newArrayList();

    // Methods.

    public void addEqualMailSpecification(@NonNull String mail) {
        userBasicSpecifications.add(UserSpecifications.hasEmail(mail));
    }

    public void addEqualPseudoSpecification(@NonNull String pseudo) {
        userBasicSpecifications.add(UserSpecifications.hasPseudo(pseudo));
    }

    public void addEqualMaterialTypesSpecification(@NonNull List<MaterialType> materialTypes) {
        for (MaterialType materialType : materialTypes) {
            userMaterialSpecifications.add(UserSpecifications.hasToolForMaterial(materialType));
        }
    }

    public void addMaterialWithColorSpecifications(@NonNull List<String> colors) {
        for (String color : colors) {
            userMaterialColorSpecifications.add(UserSpecifications.hasToolWithMaterialWithColor(color));
        }
    }

    public void addPrinter3DTypeSpecification(@NonNull List<Printer3DType> printer3DTypes) {
        for (Printer3DType printer3DType : printer3DTypes) {
            userPrinter3DTypeSpecifications.add(UserSpecifications.hasPrinter3DWithType(printer3DType));
        }
    }

    public Optional<Specification<User>> build() {
        List<Specification<User>> allSpecifications = Lists.newArrayList();

        var userBasicSpecification = combineWithAnd(userBasicSpecifications);
        userBasicSpecification.ifPresent(allSpecifications::add);

        var userMaterialSpecification = combineWithOr(userMaterialSpecifications);
        userMaterialSpecification.ifPresent(allSpecifications::add);

        var userMaterialColorSpecification = combineWithOr(userMaterialColorSpecifications);
        userMaterialColorSpecification.ifPresent(allSpecifications::add);

        var userPrinter3DTypeSpecification = combineWithOr(userPrinter3DTypeSpecifications);
        userPrinter3DTypeSpecification.ifPresent(allSpecifications::add);

        if (allSpecifications.isEmpty()) {
            return Optional.empty();
        }

        var res = Specification.where(allSpecifications.get(0));
        for (Specification<User> specification : allSpecifications) {
            res = Specification.where(res).and(specification);
        }

        return Optional.of(res);
    }

    private static Optional<Specification<User>> combineWithAnd(List<Specification<User>> userSpecifications) {
        if (userSpecifications.isEmpty())
            return Optional.empty();

        Specification<User> res = userSpecifications.get(0);
        for (int i = 1; i < userSpecifications.size(); i++) {
            res = Specification.where(res).and(userSpecifications.get(i));
        }

        return Optional.of(res);
    }

    private static Optional<Specification<User>> combineWithOr(List<Specification<User>> userSpecifications) {
        if (userSpecifications.isEmpty())
            return Optional.empty();

        Specification<User> res = userSpecifications.get(0);
        for (int i = 1; i < userSpecifications.size(); i++) {
            res = Specification.where(res).or(userSpecifications.get(i));
        }

        return Optional.of(res);
    }
}
