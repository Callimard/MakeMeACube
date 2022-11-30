package org.callimard.makemeacube.models.dto;

import com.google.common.base.Objects;
import lombok.*;

import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MakerToolDTO {

    // Variables.

    private final Integer id;
    private final String name;
    private final String description;
    private final String reference;
    private final List<MaterialDTO> materials;

    private final Integer quantity;

    // Methods.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MakerToolDTO that)) return false;
        return Objects.equal(id, that.id) && Objects.equal(name, that.name) &&
                Objects.equal(description, that.description) && Objects.equal(reference, that.reference) &&
                Objects.equal(materials, that.materials) && Objects.equal(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, reference, materials, quantity);
    }
}
