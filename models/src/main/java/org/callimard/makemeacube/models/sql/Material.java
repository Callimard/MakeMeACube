package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import lombok.*;
import org.callimard.makemeacube.models.dto.DTOSerializable;
import org.callimard.makemeacube.models.dto.MaterialDTO;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Material")
public class Material implements DTOSerializable<MaterialDTO> {

    // Constants.

    public static final String MATERIAL_ID = "id";
    public static final String MATERIAL_TYPE = "type";
    public static final String MATERIAL_COLORS = "colors";
    public static final String MATERIAL_DESCRIPTION = "description";
    public static final String MATERIAL_MAKER_TOOL = "tool";

    // Variables.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = MATERIAL_ID, nullable = false)
    private Integer id;

    @Column(name = MATERIAL_TYPE, nullable = false)
    private MaterialType type;

    @Column(name = MATERIAL_COLORS)
    private String colors;

    @Column(name = MATERIAL_DESCRIPTION)
    private String description;

    @ManyToOne
    @JoinColumn(name = MATERIAL_MAKER_TOOL)
    private MakerTool tool;

    // Methods.

    @Override
    public MaterialDTO toDTO() {
        return new MaterialDTO(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material material)) return false;
        return type == material.type && Objects.equal(colors, material.colors) &&
                Objects.equal(description, material.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, colors, description);
    }
}
