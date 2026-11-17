package com.github.virdis.ms.space.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Tipo de espaço disponibilizado para implantação de uma horta urbana"
)
public enum SpaceType {

    @Schema(
            description = "Terreno disponível para cultivo"
    )
    LAND,

    @Schema(
            description = "Terraço disponível para implantação de horta"
    )
    TERRACE,

    @Schema(
            description = "Cobertura de edifício disponível para implantação de horta"
    )
    ROOFTOP
}