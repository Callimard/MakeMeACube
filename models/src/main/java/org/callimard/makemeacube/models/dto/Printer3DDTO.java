package org.callimard.makemeacube.models.dto;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import org.callimard.makemeacube.models.sql.Material;
import org.callimard.makemeacube.models.sql.Printer3D;
import org.callimard.makemeacube.models.sql.Printer3DType;

import java.util.List;

@ToString(callSuper = true)
@Getter
public class Printer3DDTO extends MakerToolDTO {

    // Variables.

    private final Integer x;
    private final Integer y;
    private final Integer z;
    private final Integer xAccuracy;
    private final Integer yAccuracy;
    private final Integer zAccuracy;
    private final String resolution;
    private final Printer3DType type;

    // Constructors.

    public Printer3DDTO(Integer id, String name, String description, String reference, List<MaterialDTO> materials, Integer quantity,
                        Integer x, Integer y, Integer z, Integer xAccuracy, Integer yAccuracy, Integer zAccuracy, String resolution,
                        Printer3DType type) {
        super(id, name, description, reference, materials, quantity);
        this.x = x;
        this.y = y;
        this.z = z;
        this.xAccuracy = xAccuracy;
        this.yAccuracy = yAccuracy;
        this.zAccuracy = zAccuracy;
        this.resolution = resolution;
        this.type = type;
    }

    public Printer3DDTO(Printer3D printer3D) {
        this(printer3D.getId(), printer3D.getName(), printer3D.getDescription(), printer3D.getReference(),
             printer3D.getMaterials().stream().map(Material::toDTO).toList(), printer3D.getQuantity(), printer3D.getX(), printer3D.getY(),
             printer3D.getZ(), printer3D.getXAccuracy(), printer3D.getYAccuracy(), printer3D.getZAccuracy(), printer3D.getResolution(),
             printer3D.getType());
    }

    // Methods.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Printer3DDTO that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equal(x, that.x) && Objects.equal(y, that.y) &&
                Objects.equal(z, that.z) && Objects.equal(xAccuracy, that.xAccuracy) &&
                Objects.equal(yAccuracy, that.yAccuracy) && Objects.equal(zAccuracy, that.zAccuracy) &&
                Objects.equal(resolution, that.resolution) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), x, y, z, xAccuracy, yAccuracy, zAccuracy, resolution, type);
    }
}
