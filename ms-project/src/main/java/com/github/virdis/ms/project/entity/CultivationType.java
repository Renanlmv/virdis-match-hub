package com.github.virdis.ms.project.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Tipo principal de cultivo previsto no projeto"
)
public enum CultivationType {

    @Schema(
            description = "Cultivo de hortaliças e vegetais"
    )
    VEGETABLES,

    @Schema(
            description = "Cultivo de ervas e temperos"
    )
    HERBS,

    @Schema(
            description = "Cultivo de frutas"
    )
    FRUITS,

    @Schema(
            description = "Projeto com diferentes tipos de cultivo"
    )
    MIXED
}