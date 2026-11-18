package com.github.virdis.ms.project.dto;

import com.github.virdis.ms.project.entity.CultivationProject;
import com.github.virdis.ms.project.entity.CultivationType;
import com.github.virdis.ms.project.entity.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        description = "Dados retornados pela API para uma proposta de cultivo"
)
public record CultivationProjectResponseDTO(

        @Schema(
                description = "Identificador único da proposta",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Identificador do espaço anunciado no ms-space",
                example = "1"
        )
        Long spaceId,

        @Schema(
                description = "Nome do produtor responsável pela proposta",
                example = "Lucas Almeida"
        )
        String producerName,

        @Schema(
                description = "E-mail do produtor responsável pela proposta",
                example = "lucas.almeida@email.com"
        )
        String producerEmail,

        @Schema(
                description = "Tipo principal de cultivo previsto",
                example = "VEGETABLES"
        )
        CultivationType cultivationType,

        @Schema(
                description = "Descrição da proposta de implantação e cultivo",
                example = """
                        Implantação de horta com hortaliças orgânicas
                        e sistema de irrigação.
                        """
        )
        String proposalDescription,

        @Schema(
                description = "Capacidade estimada de produção mensal em quilogramas",
                example = "350"
        )
        Integer estimatedCapacity,

        @Schema(
                description = "Valor mensal oferecido pelo uso do espaço",
                example = "1800.00"
        )
        BigDecimal offeredMonthlyRent,

        @Schema(
                description = "Situação atual da proposta",
                example = "CREATED"
        )
        ProjectStatus status,

        @Schema(
                description = "Data e hora de criação da proposta",
                example = "2026-11-05T14:30:00"
        )
        LocalDateTime createdAt

) {

    public CultivationProjectResponseDTO(
            CultivationProject project
    ) {
        this(
                project.getId(),
                project.getSpaceId(),
                project.getProducerName(),
                project.getProducerEmail(),
                project.getCultivationType(),
                project.getProposalDescription(),
                project.getEstimatedCapacity(),
                project.getOfferedMonthlyRent(),
                project.getStatus(),
                project.getCreatedAt()
        );
    }
}