package org.callimard.makemeacube.models.sql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecifications {

    // Methods.

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<String>get(User.USER_MAIL), email);

    }

    public static Specification<User> hasPseudo(String pseudo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<String>get(User.USER_PSEUDO), pseudo);
    }

    public static Specification<User> hasToolForMaterial(MaterialType materialType) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<User, MakerTool> toolUser = root.join(User.USER_TOOLS);
            Join<MakerTool, Material> materialMakerTool = toolUser.join(MakerTool.MAKER_TOOL_MATERIALS);
            return criteriaBuilder.equal(materialMakerTool.get(Material.MATERIAL_TYPE), materialType);
        };
    }

    public static Specification<User> hasToolWithMaterialWithColor(String color) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<User, MakerTool> toolUser = root.join(User.USER_TOOLS);
            Join<MakerTool, Material> materialMakerTool = toolUser.join(MakerTool.MAKER_TOOL_MATERIALS);
            return criteriaBuilder.like(materialMakerTool.get(Material.MATERIAL_COLORS), "%" + color + "%");
        };
    }

    public static Specification<User> hasPrinter3DWithType(Printer3DType printer3DType) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<User, MakerTool> toolUser = root.join(User.USER_TOOLS);
            var print3DUser = criteriaBuilder.treat(toolUser, Printer3D.class);
            return criteriaBuilder.equal(print3DUser.get(Printer3D.PRINTER_3D_TYPE), printer3DType);
        };
    }
}
