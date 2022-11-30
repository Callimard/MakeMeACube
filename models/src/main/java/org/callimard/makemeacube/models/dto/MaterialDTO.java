package org.callimard.makemeacube.models.dto;

import org.callimard.makemeacube.models.sql.Material;
import org.callimard.makemeacube.models.sql.MaterialType;

public record MaterialDTO(Integer id, MaterialType type, String colors, String description) {

    public MaterialDTO(Material material) {
        this(material.getId(), material.getType(), material.getColors(), material.getDescription());
    }
}
