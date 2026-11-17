package com.github.virdis.ms.space.dto;

import com.github.virdis.ms.space.entity.SpaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(
        description = "Dados para cadastro ou atualização de um espaço"
)
public record SpaceRequestDTO(

        @Schema(
                description = "Nome do responsável pelo espaço",
                example = "Condomínio Cidade Verde"
        )
        @NotBlank(message = "O nome do responsável é obrigatório")
        String ownerName,

        @Schema(
                description = "Título de identificação do espaço",
                example = "Cobertura disponível para horta urbana"
        )
        @NotBlank(message = "O título é obrigatório")
        String title,

        @Schema(
                description = "Tipo do espaço disponibilizado",
                example = "ROOFTOP"
        )
        @NotNull(message = "O tipo do espaço é obrigatório")
        SpaceType type,

        @Schema(
                description = "Área disponível em metros quadrados",
                example = "240.50"
        )
        @NotNull(message = "A área disponível é obrigatória")
        @DecimalMin(
                value = "1.0",
                message = "A área disponível deve ser maior ou igual a 1 m²"
        )
        BigDecimal areaSquareMeters,

        @Schema(
                description = "Quantidade de residências que poderão ser beneficiadas",
                example = "60"
        )
        @NotNull(message = "A quantidade de residências é obrigatória")
        @PositiveOrZero(
                message = "A quantidade de residências não pode ser negativa"
        )
        Integer residenceCount,

        @Schema(
                description = "Cidade onde o espaço está localizado",
                example = "São Paulo"
        )
        @NotBlank(message = "A cidade é obrigatória")
        String city,

        @Schema(
                description = "Sigla do estado onde o espaço está localizado",
                example = "SP"
        )
        @NotBlank(message = "O estado é obrigatório")
        @Pattern(
                regexp = "^[A-Za-z]{2}$",
                message = "O estado deve possuir exatamente duas letras"
        )
        String state

) {
}
