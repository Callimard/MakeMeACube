package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import lombok.*;
import org.callimard.makemeacube.models.dto.DTOSerializable;
import org.callimard.makemeacube.models.dto.MakerToolDTO;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MakerTool")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "makerToolType")
public abstract class MakerTool implements DTOSerializable<MakerToolDTO> {

    // Constants.

    public static final String MAKER_TOOL_ID = "id";
    public static final String MAKER_TOOL_OWNER = "owner";
    public static final String MAKER_TOOL_NAME = "name";
    public static final String MAKER_TOOL_DESCRIPTION = "description";
    public static final String MAKER_TOOL_REFERENCE = "reference";

    // Variables.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = MAKER_TOOL_ID, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = MAKER_TOOL_OWNER)
    private User owner;

    @Column(name = MAKER_TOOL_NAME, nullable = false)
    private String name;

    @Column(name = MAKER_TOOL_DESCRIPTION, nullable = false)
    private String description;

    @Column(name = MAKER_TOOL_REFERENCE)
    private String reference;

    @ToString.Exclude
    @OneToMany(targetEntity = Material.class, mappedBy = Material.MATERIAL_MAKER_TOOL)
    private List<Material> materials;


    // Methods.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MakerTool makerTool)) return false;
        return Objects.equal(name, makerTool.name) &&
                Objects.equal(description, makerTool.description) &&
                Objects.equal(materials, makerTool.materials) &&
                Objects.equal(reference, makerTool.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, description, materials, reference);
    }
}
