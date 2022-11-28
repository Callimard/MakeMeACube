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

    public static final String PRINTER_3D_WIDTH = "width";
    public static final String PRINTER_3D_LENGTH = "length";
    public static final String PRINTER_3D_HEIGHT = "height";

    // Variables.

    @Column(name = PRINTER_3D_WIDTH, nullable = false)
    private Integer width;

    @Column(name = PRINTER_3D_LENGTH, nullable = false)
    private Integer length;

    @Column(name = PRINTER_3D_HEIGHT, nullable = false)
    private Integer height;

    // Methods.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Printer3D printer3D)) return false;
        if (!super.equals(o)) return false;
        return Objects.equal(width, printer3D.width) && Objects.equal(length, printer3D.length) &&
                Objects.equal(height, printer3D.height);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), width, length, height);
    }
}
