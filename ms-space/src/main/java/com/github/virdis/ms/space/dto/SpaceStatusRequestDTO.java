package com.github.virdis.ms.space.dto;

import com.github.virdis.ms.space.entity.SpaceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        description = "Dados para alteração do status de um espaço"
)
public record SpaceStatusRequestDTO(

        @Schema(
                description = "Novo status do espaço",
                example = "MATCHED"
        )
        @NotNull(message = "O status é obrigatório")
        SpaceStatus status

) {
}