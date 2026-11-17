package com.github.virdis.ms.space.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = """
                Situação atual do espaço disponibilizado para implantação
                de uma horta urbana.
                """
)
public enum SpaceStatus {

    @Schema(
            description = """
                    Espaço disponível para receber propostas de implantação.
                    """
    )
    AVAILABLE,

    @Schema(
            description = """
                    Espaço associado a um projeto que está em análise.
                    """
    )
    UNDER_EVALUATION,

    @Schema(
            description = """
                    Espaço vinculado a um projeto de implantação aprovado.
                    """
    )
    MATCHED,

    @Schema(
            description = """
                    Espaço temporariamente ou permanentemente indisponível.
                    """
    )
    UNAVAILABLE
}
