package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.callimard.makemeacube.models.dto.Printer3DDTO;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Printer3D")
@DiscriminatorValue("P3D")
public class Printer3D extends MakerTool {

    // Constants.

    public static final String PRINTER_3D_X = "x";
    public static final String PRINTER_3D_Y = "y";
    public static final String PRINTER_3D_Z = "z";
    public static final String PRINTER_3D_X_ACCURACY = "xAccuracy";
    public static final String PRINTER_3D_Y_ACCURACY = "yAccuracy";
    public static final String PRINTER_3D_Z_ACCURACY = "zAccuracy";
    public static final String PRINTER_3D_LAYER_THICKNESS = "layerThickness";
    public static final String PRINTER_3D_TYPE = "type";

    // Variables.

    @Column(name = PRINTER_3D_X, nullable = false)
    private Integer x;

    @Column(name = PRINTER_3D_Y, nullable = false)
    private Integer y;

    @Column(name = PRINTER_3D_Z, nullable = false)
    private Integer z;

    @Column(name = PRINTER_3D_X_ACCURACY, nullable = false)
    private Integer xAccuracy;

    @Column(name = PRINTER_3D_Y_ACCURACY, nullable = false)
    private Integer yAccuracy;

    @Column(name = PRINTER_3D_Z_ACCURACY, nullable = false)
    private Integer zAccuracy;

    @Column(name = PRINTER_3D_LAYER_THICKNESS, nullable = false)
    private Integer layerThickness;

    @Column(name = PRINTER_3D_TYPE, nullable = false)
    private Printer3DType type;

    // Constructors.

    public Printer3D() {
        super();
    }

    public Printer3D(Integer id, User owner, String name, String description, String reference, List<Material> materials, Integer x, Integer y,
                     Integer z, Integer xAccuracy, Integer yAccuracy, Integer zAccuracy, Integer layerThickness, Printer3DType type) {
        super(id, owner, name, description, reference, materials);
        this.x = x;
        this.y = y;
        this.z = z;
        this.xAccuracy = xAccuracy;
        this.yAccuracy = yAccuracy;
        this.zAccuracy = zAccuracy;
        this.layerThickness = layerThickness;
        this.type = type;
    }

    // Methods.

    @Override
    public Printer3DDTO toDTO() {
        return new Printer3DDTO(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Printer3D printer3D)) return false;
        if (!super.equals(o)) return false;
        return Objects.equal(x, printer3D.x) && Objects.equal(y, printer3D.y) &&
                Objects.equal(z, printer3D.z) && Objects.equal(xAccuracy, printer3D.xAccuracy) &&
                Objects.equal(yAccuracy, printer3D.yAccuracy) &&
                Objects.equal(zAccuracy, printer3D.zAccuracy) &&
                Objects.equal(layerThickness, printer3D.layerThickness) &&
                Objects.equal(type, printer3D.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), x, y, z, xAccuracy, yAccuracy, zAccuracy, layerThickness, type);
    }
}
