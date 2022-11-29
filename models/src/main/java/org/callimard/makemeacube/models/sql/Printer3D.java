package org.callimard.makemeacube.models.sql;

import com.google.common.base.Objects;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
    public static final String PRINTER_3D_TYPE = "layerThickness";

    // Variables.

    @Column(name = PRINTER_3D_X, nullable = false)
    private Integer x;

    @Column(name = PRINTER_3D_Y, nullable = false)
    private Integer y;

    @Column(name = PRINTER_3D_Z, nullable = false)
    private Integer z;

    @Column(name = PRINTER_3D_X_ACCURACY, nullable = false)
    private Double xAccuracy;

    @Column(name = PRINTER_3D_Y_ACCURACY, nullable = false)
    private Double yAccuracy;

    @Column(name = PRINTER_3D_Z_ACCURACY, nullable = false)
    private Double zAccuracy;

    @Column(name = PRINTER_3D_LAYER_THICKNESS, nullable = false)
    private Double layerThickness;

    @Column(name = PRINTER_3D_TYPE, nullable = false)
    private Printer3DType type;

    // Methods.

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
