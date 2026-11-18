package com.github.virdis.ms.project.dto;

import com.github.virdis.ms.project.entity.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        description = "Dados para alteração do status de uma proposta de cultivo"
)
public record ProjectStatusRequestDTO(

        @Schema(
                description = "Novo status da proposta",
                example = "ANALYZING"
        )
        @NotNull(message = "O status da proposta é obrigatório")
        ProjectStatus status

) {
}