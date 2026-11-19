package com.github.virdis.ms.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        description = "Dados enviados ao ms-space para alteração do status de um espaço"
)
public record SpaceStatusRequestDTO(

        @Schema(
                description = "Novo status do espaço",
                example = "UNDER_EVALUATION"
        )
        @NotBlank(message = "O status do espaço é obrigatório")
        String status

) {
}