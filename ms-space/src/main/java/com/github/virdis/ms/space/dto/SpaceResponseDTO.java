package com.github.virdis.ms.space.dto;

import com.github.virdis.ms.space.entity.Space;
import com.github.virdis.ms.space.entity.SpaceStatus;
import com.github.virdis.ms.space.entity.SpaceType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        description = "Dados retornados pela API para um espaço"
)
public record SpaceResponseDTO(

        @Schema(
                description = "Identificador único do espaço",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome do responsável pelo espaço",
                example = "Condomínio Cidade Verde"
        )
        String ownerName,

        @Schema(
                description = "Título de identificação do espaço",
                example = "Cobertura disponível para horta urbana"
        )
        String title,

        @Schema(
                description = "Tipo do espaço disponibilizado",
                example = "ROOFTOP"
        )
        SpaceType type,

        @Schema(
                description = "Área disponível em metros quadrados",
                example = "240.50"
        )
        BigDecimal areaSquareMeters,

        @Schema(
                description = "Quantidade de residências que poderão ser beneficiadas",
                example = "60"
        )
        Integer residenceCount,

        @Schema(
                description = "Cidade onde o espaço está localizado",
                example = "São Paulo"
        )
        String city,

        @Schema(
                description = "Sigla do estado onde o espaço está localizado",
                example = "SP"
        )
        String state,

        @Schema(
                description = "Situação atual do espaço",
                example = "AVAILABLE"
        )
        SpaceStatus status,

        @Schema(
                description = "Data e hora de cadastro do espaço",
                example = "2026-11-05T14:30:00"
        )
        LocalDateTime createdAt

) {

    public SpaceResponseDTO(Space space) {
        this(
                space.getId(),
                space.getOwnerName(),
                space.getTitle(),
                space.getType(),
                space.getAreaSquareMeters(),
                space.getResidenceCount(),
                space.getCity(),
                space.getState(),
                space.getStatus(),
                space.getCreatedAt()
        );
    }
}
