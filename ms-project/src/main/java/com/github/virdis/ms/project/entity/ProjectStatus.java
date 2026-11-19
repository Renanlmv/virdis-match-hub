package com.github.virdis.ms.project.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Situação atual do projeto de implantação da horta urbana"
)
public enum ProjectStatus {

    @Schema(
            description = "Projeto criado e aguardando o início da análise"
    )
    CREATED,

    @Schema(
            description = "Projeto em processo de análise"
    )
    ANALYZING,

    @Schema(
            description = "Projeto aprovado para implantação"
    )
    APPROVED,

    @Schema(
            description = "Projeto rejeitado durante a análise"
    )
    REJECTED,

    @Schema(
            description = "Projeto cancelado"
    )
    CANCELED
}