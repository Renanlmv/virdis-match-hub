package com.github.virdis.ms.project.dto;

import com.github.virdis.ms.project.entity.CultivationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(
        description = "Dados para cadastro ou atualização de uma proposta de cultivo"
)
public record CultivationProjectRequestDTO(

        @Schema(
                description = "Identificador do espaço anunciado no ms-space",
                example = "1"
        )
        @NotNull(message = "O identificador do espaço é obrigatório")
        @Positive(message = "O identificador do espaço deve ser maior que zero")
        Long spaceId,

        @Schema(
                description = "Nome do produtor responsável pela proposta",
                example = "Lucas Almeida"
        )
        @NotBlank(message = "O nome do produtor é obrigatório")
        String producerName,

        @Schema(
                description = "E-mail do produtor responsável pela proposta",
                example = "lucas.almeida@email.com"
        )
        @NotBlank(message = "O e-mail do produtor é obrigatório")
        @Email(message = "O e-mail do produtor deve ser válido")
        String producerEmail,

        @Schema(
                description = "Tipo principal de cultivo previsto",
                example = "VEGETABLES"
        )
        @NotNull(message = "O tipo de cultivo é obrigatório")
        CultivationType cultivationType,

        @Schema(
                description = "Descrição da proposta de implantação e cultivo",
                example = "Implantação de horta com hortaliças orgânicas e sistema de irrigação."
        )
        @NotBlank(message = "A descrição da proposta é obrigatória")
        @Size(
                max = 1000,
                message = "A descrição deve possuir no máximo 1000 caracteres"
        )
        String proposalDescription,

        @Schema(
                description = "Capacidade estimada de produção mensal em quilogramas",
                example = "350"
        )
        @NotNull(message = "A capacidade estimada é obrigatória")
        @Positive(message = "A capacidade estimada deve ser maior que zero")
        Integer estimatedCapacity,

        @Schema(
                description = "Valor mensal oferecido pelo uso do espaço",
                example = "1800.00"
        )
        @NotNull(message = "O valor mensal oferecido é obrigatório")
        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "O valor mensal oferecido não pode ser negativo"
        )
        BigDecimal offeredMonthlyRent

) {
}